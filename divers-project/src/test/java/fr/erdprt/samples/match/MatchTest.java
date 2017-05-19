package fr.erdprt.samples.match;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class MatchTest {

	private static List<String> listAlterParametres = Arrays.asList("UPDATE T_PRM_", "INSERT FROM T_PRM_", "DELETE FROM T_PRM_");


	@Test
	public void alterParametresOldLoop() {

		int count = 10000;
		long start = System.currentTimeMillis();
		for (int index = 0; index < count; index++) {
			switch ((index % 3)) {
				case 0:
					alterParametresOld("update T_PRM_");
					break;
				case 1:
					alterParametresOld("insert into T_PRM_");
					break;
				case 2:
					alterParametresOld("delete from T_PRM_");
					break;
				default:
					break;
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("duration for " + count + ":" + (end - start) + " ms");
	}

	@Test
	public void alterParametresLoop() {

		int count = 10000;
		long start = System.currentTimeMillis();
		for (int index = 0; index < count; index++) {
			switch ((index % 3)) {
				case 0:
					alterParametres("update  T_PRM_");
					break;
				case 1:
					alterParametres("insert  into  T_PRM_");
					break;
				case 2:
					alterParametres("delete  from  T_PRM_");
					break;
				default:
					break;
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("duration for " + count + ":" + (end - start) + " ms");
	}

	@Test
	public void alterParametresStringUtilsLoop() {

		int count = 10000;
		long start = System.currentTimeMillis();
		for (int index = 0; index < count; index++) {
			switch ((index % 3)) {
				case 0:
					alterParametresStringUtils("update  T_PRM_");
					break;
				case 1:
					alterParametresStringUtils("insert  into  T_PRM_");
					break;
				case 2:
					alterParametresStringUtils("delete  from  T_PRM_");
					break;
				default:
					break;
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("duration for " + count + ":" + (end - start) + " ms");
	}

	public boolean alterParametresOld(final String sql) {
		// A reprendre: un peu brut: regexp?
		String sqlF = sql.toUpperCase().trim();
		if (sqlF.startsWith("UPDATE T_PRM_") || sqlF.startsWith("INSERT INTO T_PRM_") || sqlF.startsWith("DELETE FROM T_PRM_")) {
			return true;
		}
		return false;
	}

	public boolean alterParametres(final String sql) {
		if (!Pattern.compile("UPDATE( +)T_PRM_").matcher(sql.toUpperCase().trim()).matches()) {
			if (!Pattern.compile("INSERT( +)INTO( +)T_PRM_").matcher(sql.toUpperCase().trim()).matches()) {
				if (!Pattern.compile("DELETE( +)FROM( +)T_PRM_").matcher(sql.toUpperCase().trim()).matches()) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean alterParametresStringUtils(final String sql) {
		String sqlF = StringUtils.replacePattern(sql.toUpperCase().trim(), "( +)", " ");
		boolean alterParametres = false;
		for (String sqlCurrent : listAlterParametres) {
			if (sqlF.startsWith(sqlCurrent))
				alterParametres = true;
			break;
		}
		return alterParametres;
	}

}
