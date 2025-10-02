(function () {
  "use strict";

  /**
   * JSONPlaceholder Explorer
   * - Category navigation (posts, comments, albums, photos, todos, users)
   * - Server-side pagination using _page and _limit
   * - Search via q parameter
   * - Category-specific renderers
   */

  /** @type {const} */
  const BASE_URL = "https://jsonplaceholder.typicode.com";

  /** @type {const} */
  const CATEGORIES = [
    { id: "posts", label: "Posts" },
    { id: "comments", label: "Comments" },
    { id: "albums", label: "Albums" },
    { id: "photos", label: "Photos" },
    { id: "todos", label: "Todos" },
    { id: "users", label: "Users" },
  ];

  const state = {
    currentCategory: "posts",
    currentPage: 1,
    pageSize: 10,
    searchQuery: "",
    isLoading: false,
    abortController: null,
    totalCount: 0,
  };

  /** DOM Elements */
  const resultsEl = /** @type {HTMLElement} */ (document.getElementById("results"));
  const paginationEl = /** @type {HTMLElement} */ (document.getElementById("pagination"));
  const searchInputEl = /** @type {HTMLInputElement} */ (document.getElementById("searchInput"));
  const pageSizeEl = /** @type {HTMLSelectElement} */ (document.getElementById("pageSize"));
  const loadingOverlayEl = /** @type {HTMLElement} */ (document.getElementById("loadingOverlay"));
  const categoryButtons = /** @type {NodeListOf<HTMLButtonElement>} */ (document.querySelectorAll(".category-btn"));

  function init() {
    // Wire category buttons
    categoryButtons.forEach((btn) => {
      btn.addEventListener("click", () => {
        const category = btn.getAttribute("data-category");
        if (!category || category === state.currentCategory) return;
        state.currentCategory = category;
        state.currentPage = 1;
        setActiveCategoryButton(category);
        fetchAndRender();
      });
    });

    // Wire search with debounce
    let searchDebounceTimer = 0;
    searchInputEl.addEventListener("input", () => {
      window.clearTimeout(searchDebounceTimer);
      searchDebounceTimer = window.setTimeout(() => {
        state.searchQuery = searchInputEl.value.trim();
        state.currentPage = 1;
        fetchAndRender();
      }, 300);
    });

    // Wire page size
    pageSizeEl.addEventListener("change", () => {
      const newSize = parseInt(pageSizeEl.value, 10);
      if (isFinite(newSize) && newSize > 0) {
        state.pageSize = newSize;
        state.currentPage = 1;
        fetchAndRender();
      }
    });

    // Initial load
    setActiveCategoryButton(state.currentCategory);
    fetchAndRender();
  }

  function setActiveCategoryButton(categoryId) {
    categoryButtons.forEach((btn) => {
      const id = btn.getAttribute("data-category");
      if (id === categoryId) {
        btn.setAttribute("aria-current", "page");
      } else {
        btn.removeAttribute("aria-current");
      }
    });
  }

  function setLoading(isLoading) {
    state.isLoading = isLoading;
    resultsEl.setAttribute("aria-busy", String(isLoading));
    loadingOverlayEl.hidden = !isLoading;
  }

  function buildUrl() {
    const url = new URL(`${BASE_URL}/${state.currentCategory}`);
    url.searchParams.set("_page", String(state.currentPage));
    url.searchParams.set("_limit", String(state.pageSize));
    if (state.searchQuery) {
      url.searchParams.set("q", state.searchQuery);
    }
    return url.toString();
  }

  async function fetchAndRender() {
    if (state.abortController) {
      try { state.abortController.abort(); } catch {}
    }
    const ac = new AbortController();
    state.abortController = ac;

    setLoading(true);
    try {
      const url = buildUrl();
      const response = await fetch(url, {
        method: "GET",
        headers: { Accept: "application/json" },
        signal: ac.signal,
      });

      if (!response.ok) {
        throw new Error(`Request failed with ${response.status}`);
      }

      const totalHeader = response.headers.get("X-Total-Count");
      state.totalCount = totalHeader ? parseInt(totalHeader, 10) || 0 : 0;

      /** @type {any[]} */
      const data = await response.json();

      renderResults(data);
      renderPagination();
    } catch (err) {
      if (err && typeof err === "object" && "name" in err && err.name === "AbortError") {
        return; // ignore
      }
      renderError(err instanceof Error ? err.message : "Unknown error");
    } finally {
      setLoading(false);
    }
  }

  function clearNode(el) {
    while (el.firstChild) el.removeChild(el.firstChild);
  }

  function renderError(message) {
    clearNode(resultsEl);
    resultsEl.classList.remove("photos");

    const card = createElement("div", "card");
    const heading = createElement("h3");
    heading.textContent = "Error";
    const p = createElement("p");
    p.textContent = message;

    card.appendChild(heading);
    card.appendChild(p);
    resultsEl.appendChild(card);
    clearNode(paginationEl);
  }

  function renderResults(items) {
    clearNode(resultsEl);
    resultsEl.classList.toggle("photos", state.currentCategory === "photos");

    switch (state.currentCategory) {
      case "posts":
        renderPosts(items);
        break;
      case "comments":
        renderComments(items);
        break;
      case "albums":
        renderAlbums(items);
        break;
      case "photos":
        renderPhotos(items);
        break;
      case "todos":
        renderTodos(items);
        break;
      case "users":
        renderUsers(items);
        break;
      default:
        renderGeneric(items);
    }
  }

  function renderPagination() {
    clearNode(paginationEl);

    const total = state.totalCount;
    const perPage = state.pageSize;
    const current = state.currentPage;

    // When server does not provide X-Total-Count (e.g., /users with q param?),
    // fall back to a simple prev/next without numbers.
    const totalPages = total > 0 ? Math.max(1, Math.ceil(total / perPage)) : null;

    const makeBtn = (label, onClick, opts = {}) => {
      const btn = createElement("button", "page-btn");
      btn.type = "button";
      btn.textContent = label;
      if (opts.disabled) btn.disabled = true;
      if (opts.current) btn.setAttribute("aria-current", "page");
      btn.addEventListener("click", onClick);
      return btn;
    };

    // Prev
    paginationEl.appendChild(
      makeBtn("Prev", () => {
        if (state.currentPage > 1) {
          state.currentPage -= 1;
          fetchAndRender();
        }
      }, { disabled: totalPages ? current <= 1 : false })
    );

    if (totalPages) {
      // Show up to 7 page buttons with ellipses
      const pages = buildCompactPageList(current, totalPages, 7);
      for (const p of pages) {
        if (p === "...") {
          const span = createElement("span", "page-btn");
          span.textContent = "…";
          span.setAttribute("aria-hidden", "true");
          paginationEl.appendChild(span);
        } else {
          paginationEl.appendChild(
            makeBtn(String(p), () => {
              if (state.currentPage !== p) {
                state.currentPage = p;
                fetchAndRender();
              }
            }, { current: p === current })
          );
        }
      }
    }

    // Next
    paginationEl.appendChild(
      makeBtn("Next", () => {
        if (!totalPages || state.currentPage < totalPages) {
          state.currentPage += 1;
          fetchAndRender();
        }
      }, { disabled: totalPages ? current >= totalPages : false })
    );
  }

  function buildCompactPageList(current, total, maxButtons) {
    const pages = [];
    const visible = Math.max(5, maxButtons | 0);

    if (total <= visible) {
      for (let i = 1; i <= total; i++) pages.push(i);
      return pages;
    }

    const side = 1; // keep first and last always
    const windowSize = visible - 2 * side - 2; // minus first,last and two ellipses
    const start = Math.max(2, current - Math.floor(windowSize / 2));
    const end = Math.min(total - 1, start + windowSize - 1);

    pages.push(1);
    if (start > 2) pages.push("...");
    for (let i = start; i <= end; i++) pages.push(i);
    if (end < total - 1) pages.push("...");
    pages.push(total);

    return pages;
  }

  // Renderers
  function renderPosts(posts) {
    for (const post of posts) {
      const card = createElement("div", "card");

      const h3 = createElement("h3");
      h3.textContent = safeText(post.title);

      const p = createElement("p");
      p.textContent = safeText(post.body);

      const meta = createElement("div", "muted");
      meta.textContent = `User ${post.userId} · #${post.id}`;

      card.appendChild(h3);
      card.appendChild(p);
      card.appendChild(meta);
      resultsEl.appendChild(card);
    }
  }

  function renderComments(comments) {
    for (const c of comments) {
      const card = createElement("div", "card");

      const h3 = createElement("h3");
      h3.textContent = safeText(c.name);

      const meta = createElement("div", "muted");
      meta.textContent = `${c.email} · Post #${c.postId} · #${c.id}`;

      const p = createElement("p");
      p.textContent = safeText(c.body);

      card.appendChild(h3);
      card.appendChild(meta);
      card.appendChild(p);
      resultsEl.appendChild(card);
    }
  }

  function renderAlbums(albums) {
    for (const a of albums) {
      const card = createElement("div", "card");

      const h3 = createElement("h3");
      h3.textContent = safeText(a.title);

      const meta = createElement("div", "muted");
      meta.textContent = `User ${a.userId} · #${a.id}`;

      card.appendChild(h3);
      card.appendChild(meta);
      resultsEl.appendChild(card);
    }
  }

  function renderPhotos(photos) {
    for (const ph of photos) {
      const card = createElement("div", "card");

      const img = createElement("img", "photo-thumb");
      img.src = ph.thumbnailUrl || ph.url;
      img.alt = ph.title || `Photo #${ph.id}`;
      img.loading = "lazy";

      const metaWrap = createElement("div", "photo-meta");
      const h3 = createElement("h3");
      h3.textContent = safeText(ph.title);

      const meta = createElement("div", "muted");
      meta.textContent = `Album ${ph.albumId} · #${ph.id}`;

      metaWrap.appendChild(h3);
      metaWrap.appendChild(meta);
      card.appendChild(img);
      card.appendChild(metaWrap);
      resultsEl.appendChild(card);
    }
  }

  function renderTodos(todos) {
    for (const t of todos) {
      const card = createElement("div", "card");

      const h3 = createElement("h3");
      h3.textContent = safeText(t.title);

      const meta = createElement("div", "muted");
      meta.textContent = `User ${t.userId} · #${t.id}`;

      const kvs = createElement("div", "kvs");
      const kv = createElement("div", "kv");
      const key = createElement("div", "key"); key.textContent = "Completed";
      const val = createElement("div"); val.textContent = t.completed ? "Yes" : "No";
      kv.appendChild(key); kv.appendChild(val);
      kvs.appendChild(kv);

      card.appendChild(h3);
      card.appendChild(meta);
      card.appendChild(kvs);
      resultsEl.appendChild(card);
    }
  }

  function renderUsers(users) {
    for (const u of users) {
      const card = createElement("div", "card");

      const h3 = createElement("h3");
      h3.textContent = `${safeText(u.name)} (@${safeText(u.username)})`;

      const meta = createElement("div", "muted");
      meta.textContent = `#${u.id} · ${safeText(u.email)}`;

      const kvs = createElement("div", "kvs");
      appendKv(kvs, "Phone", u.phone);
      appendKv(kvs, "Website", u.website);

      if (u.address) {
        const addr = formatAddress(u.address);
        appendKv(kvs, "Address", addr);
      }
      if (u.company) {
        const company = formatCompany(u.company);
        appendKv(kvs, "Company", company);
      }

      card.appendChild(h3);
      card.appendChild(meta);
      card.appendChild(kvs);
      resultsEl.appendChild(card);
    }
  }

  function renderGeneric(items) {
    for (const it of items) {
      const card = createElement("div", "card");
      const pre = createElement("pre");
      pre.textContent = JSON.stringify(it, null, 2);
      card.appendChild(pre);
      resultsEl.appendChild(card);
    }
  }

  function appendKv(container, keyLabel, valueText) {
    const kv = createElement("div", "kv");
    const key = createElement("div", "key"); key.textContent = keyLabel;
    const val = createElement("div"); val.textContent = safeText(valueText);
    kv.appendChild(key); kv.appendChild(val);
    container.appendChild(kv);
  }

  function formatAddress(address) {
    const parts = [];
    if (address.street) parts.push(address.street);
    if (address.suite) parts.push(address.suite);
    if (address.city) parts.push(address.city);
    if (address.zipcode) parts.push(address.zipcode);
    return parts.join(", ");
  }

  function formatCompany(company) {
    const parts = [];
    if (company.name) parts.push(company.name);
    if (company.catchPhrase) parts.push(`“${company.catchPhrase}”`);
    if (company.bs) parts.push(company.bs);
    return parts.join(" · ");
  }

  function safeText(value) {
    if (value === null || value === undefined) return "";
    if (typeof value === "string") return value;
    try { return String(value); } catch { return ""; }
  }

  function createElement(tag, className, attrs) {
    const el = document.createElement(tag);
    if (className) el.className = className;
    if (attrs && typeof attrs === "object") {
      for (const [k, v] of Object.entries(attrs)) {
        if (v !== undefined && v !== null) {
          el.setAttribute(k, String(v));
        }
      }
    }
    return el;
  }

  // Boot
  document.addEventListener("DOMContentLoaded", init);
})();
