package fr.dsirc.dsn.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Classe utilitaire de manipulation de montants.<br/>
 */
public final class NumericUtils {

	/** Format pour un numeric avec deux chiffre après la virgule. */
	private static final String TWO_DIGIT_FORMAT = "0.00";

	/**
	 * Constructeur par défaut.
	 */
	private NumericUtils() {
	}

	/**
	 * Permet de formater avec deux chiffres après la virgule.
	 * @param object le numérique à formater.
	 * @return la string formater.
	 */
	public static String formaterTaux(final Double object) {
		String res = null;
		if (object != null) {
			res = new DecimalFormat(TWO_DIGIT_FORMAT).format(object) + " %";
		}
		return res;
	}

	/**
	 * Additionne deux Double potentiellement null.
	 * @param val1 le premier Double
	 * @param val2 le second Double
	 * @return la somme des 2 Double considérant que null vaut 0.
	 */
	public static Double addDouble(final Double val1, final Double val2) {
		boolean val1set = val1 != null;
		boolean val2set = val2 != null;

		if (val1set && val2set) {
			return val1 + val2;
		}

		if (val1set) {
			return val1;
		}

		return val2;
	}

	/**
	 * Donne la valeur équivalente d'un Double en double considérant que null vaut 0.0.
	 * @param val le Double à convertir.
	 * @return le double correspondant
	 */
	public static double getComparable(final Double val) {
		if (val == null) {
			return 0.0;
		}
		return val;
	}

	/**
	 * Donne la valeur équivalente d'un Integer en int considérant que null vaut 0.
	 * @param val le Double à convertir.
	 * @return le int correspondant;
	 */
	public static int getComparable(final Integer val) {
		if (val == null) {
			return 0;
		}
		return val;
	}

	/**
	 * Soustrait deux Double potentiellement null.
	 * @param val1 le premier Integer
	 * @param val2 le second Integer
	 * @return la soustraction des 2 Double considérant que null vaut 0.
	 */
	public static Double subDouble(final Double val1, final Double val2) {
		boolean val1set = val1 != null;
		boolean val2set = val2 != null;

		if (val1set && val2set) {
			return val1 - val2;
		}

		if (val1set) {
			return val1;
		}

		return -val2;
	}

	/**
	 * Additionne deux Integer potentiellement null.
	 * @param val1 le premier Integer
	 * @param val2 le second Integer
	 * @return la somme des 2 Integer considérant que null vaut 0.
	 */
	public static Integer addInteger(final Integer val1, final Integer val2) {
		boolean val1set = val1 != null;
		boolean val2set = val2 != null;

		if (val1set && val2set) {
			return val1 + val2;
		}

		if (val1set) {
			return val1;
		}

		return val2;
	}

	/**
	 * Conversion d'un double en BigDecimal
	 * @param montant
	 * @return BigDecimal
	 * @deprecated
	 */
	@Deprecated
	public static BigDecimal convertir(final Double montant) {
		if (montant != null) {
			return BigDecimal.valueOf(montant);
		}
		return null;
	}

	/**
	 * Conversion d'un double en BigDecimal
	 * @param montant le montant à convertir
	 * @param format le format de conversion: règle les pb de decimal
	 * @return un nouveau {@link BigDecimal} correspondant au montant.
	 * @deprecated
	 */
	@Deprecated
	public static BigDecimal convertir(final Double montant, final String format) {
		if (montant != null) {
			DecimalFormat decimalFormat = new DecimalFormat(format);
			String strMontant = decimalFormat.format(montant);
			return new BigDecimal(strMontant.replaceAll(",", "."));
		}
		return null;
	}

	/**
	 * Cumul d'une valeur
	 * @param operation
	 * @param montantInitial
	 * @param montantsACumuler
	 * @return le nouveau montant cumulé
	 */
	public static Double cumuler(final NumericUtilsOp operation, final Double montantInitial, final Double... montantsACumuler) {
		Double result = null;
		if (montantInitial != null) {
			result = montantInitial;
		}
		if (NumericUtilsOp.ADDITION.equals(operation)) {
			for (Double montant : montantsACumuler) {
				if (montant != null) {
					result += montant; // @Warning attention a la perte d'information lors de l'addition des objets de type Double
				}
			}
		} else if (NumericUtilsOp.SOUSTRACTION.equals(operation)) {
			for (Double montant : montantsACumuler) {
				if (montant != null) {
					result -= montant; // @Warning attention a la perte d'information lors de l'addition des objets de type Double
				}
			}
		}
		return result;
	}

	/**
	 * Cumul d'une valeur
	 * @param montantInitial montant initial
	 * @param montantsACumuler montants à cumuler
	 * @return le nouveau montant cumulé
	 */
	public static Double cumuler(final Double montantInitial, final Double... montantsACumuler) {
		double result = 0d;
		if (montantInitial != null) {
			result += montantInitial;
		}
		for (Double montant : montantsACumuler) {
			if (montant != null) {
				result += montant; // @Warning attention a la perte d'information lors de l'addition des objets de type Double
			}
		}
		return result;
	}

	public static Double cumulerPrimitive(final double montantInitial, final double... montantsACumuler) {
		double result = montantInitial;
		for (double montant : montantsACumuler) {
				result += montant; // @Warning attention a la perte d'information lors de l'addition des objets de type Double
		}
		return result;
	}

	/**
	 * Cumul d'une valeur
	 * @param montantInitial montant initial
	 * @param montantsACumuler montants à cumuler
	 * @return le nouveau montant cumulé
	 * @deprecated
	 */
	@Deprecated
	public static BigDecimal cumuler(final BigDecimal montantInitial, final BigDecimal... montantsACumuler) {
		BigDecimal result = BigDecimal.ZERO;
		if (montantInitial != null) {
			result = result.add(montantInitial);
		}
		for (BigDecimal montant : montantsACumuler) {
			result = result.add(BigDecimal.valueOf(montant.doubleValue()));
		}
		return result;
	}
}
