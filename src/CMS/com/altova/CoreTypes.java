/**
 * CoreTypes.java
 *
 * This file was generated by MapForce 2015r4sp1.
 *
 * YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
 * OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
 *
 * Refer to the MapForce Documentation for further details.
 * http://www.altova.com/mapforce
 */
package com.altova;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.lang.String;
import java.lang.ArithmeticException;
import com.altova.types.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;

public class CoreTypes
{
	public static final int DateTimeFormat_W3_dateTime = 0;		/* Format as schema dateTime */
	public static final int DateTimeFormat_W3_date = 1;			/* Format as schema date */
	public static final int DateTimeFormat_W3_time = 2;			/* Format as schema time */
	public static final int DateTimeFormat_W3_gYear = 3;		/* Format as schema gYear */
	public static final int DateTimeFormat_W3_gYearMonth = 4;	/* Format as schema gYearMonth */
	public static final int DateTimeFormat_W3_gMonth = 5;		/* Format as schema gMonth */
	public static final int DateTimeFormat_W3_gMonthDay = 6;	/* Format as schema gMonthDay */
	public static final int DateTimeFormat_W3_gDay = 7;			/* Format as schema gDay */
	public static final int DateTimeFormat_S_DateTime = 8;		/* Format as standard DateTime "YYYY-MM-DD HH:MM:SS" */
	public static final int DateTimeFormat_S_Seconds = 9;		/* Format as number of seconds since epoch */
	public static final int DateTimeFormat_S_Days = 10;			/* Format as number of days since epoch */

	static class NumberParts
	{
		String sign;
		String mantissa;
		String exponent;

		public NumberParts(String input)
		{
			input = input.trim();
			if (input.startsWith("-"))
			{
				sign = "-";
				input = input.substring(1);
			}
			else if (input.startsWith("+"))
			{
				sign = "";
				input = input.substring(1);
			}
			else
				sign = "";
			int indexOfE = input.indexOf('e');
			if (indexOfE < 0)
				indexOfE = input.indexOf('E');
			if (indexOfE < 0)
			{
				mantissa = input;
				exponent = "";
			}
			else
			{
				mantissa = input.substring(0, indexOfE);
				exponent = input.substring(indexOfE + 1);
			}

			if (mantissa.startsWith("."))
				mantissa = "0" + mantissa;
			if (mantissa.endsWith("."))
				mantissa = mantissa + "0";
			if (mantissa.length() == 0)
				mantissa = "0";
			if (exponent.endsWith("+") || exponent.endsWith("-"))
				exponent = exponent + "0";
		}

		public String toString()
		{
			if (exponent.length() > 0)
				return sign + mantissa + "E" + exponent;
			else
				return sign + mantissa;
		}

		public double toDouble()
		{
			return Double.parseDouble(toString());
		}

		public long toLong()
		{
			int dot = mantissa.indexOf('.');
			if (dot >= 0)
				return Long.parseLong(sign + mantissa.substring(0, dot));
			else
				return Long.parseLong(sign + mantissa);
		}

		public BigDecimal toBigDecimal()
		{
			return new BigDecimal(sign + mantissa);
		}

		public BigInteger toBigInteger()
		{
			int dot = mantissa.indexOf('.');
			if (dot >= 0)
				return new BigInteger(sign + mantissa.substring(0, dot));
			else
				return new BigInteger(sign + mantissa);
		}
	}

	private static boolean isSchemaSign(char c)
	{
		return c == '+' || c == '-';
	}

	private static boolean isSchemaDigit(char c)
	{
		return c >= '0' && c <= '9';
	}

	private static boolean isSchemaRadix(char c)
	{
		return c == '.';
	}

	private static boolean isSchemaExponent(char c)
	{
		return c == 'e' || c == 'E';
	}

	static int[][] PNactions = new int[][]{
		{ 0, 0, 1, 2, 2 },	
		{ 2, 0, 1, 2, 2 },	
		{ 2, 0, 0, 0, 2 },	
		{ 2, 0, 2, 2, 2 },	
		{ 2, 0, 2, 0, 2 },	
		{ 2, 0, 2, 1, 2 }, 
		{ 0, 0, 2, 2, 2 },
		{ 2, 0, 2, 2, 2 },
		{ 2, 0, 2, 2, 2 },
	};

	static int[][] PNfollow = new int[][]{ 
		{ 1, 2, 3, 9, 9 }, 
		{ 9, 2, 3, 9, 9 },
		{ 9, 2, 5, 6, 9 },
		{ 9, 4, 9, 9, 9 },
		{ 9, 4, 9, 6, 9 },
		{ 9, 4, 9, 6, 9 },
		{ 7, 8, 9, 9, 9 },
		{ 9, 8, 9, 9, 9 },
		{ 9, 8, 9, 9, 9 },
	};

	private static int prepareNumber(java.lang.StringBuffer buffer, java.lang.String input)
	{
		int state = 0;

		for (int i = 0; i != input.length(); ++i)		
		{
			char c = input.charAt(i);
			int cls;

			if (isSchemaDigit(c))
				cls = 1;
			else if (isSchemaSign(c))
				cls = 0;
			else if (isSchemaRadix(c))
				cls = 2;
			else if (isSchemaExponent(c))
				cls = 3;
			else
				cls = 4;

			switch (PNactions[state][cls])
			{
			case 0:
				buffer.append(c);
				break;
			case 1:
				buffer.append('0');
				buffer.append(c);
				break;
			case 2:
				return 0;
			}
			state = PNfollow[state][cls];
		}

		switch (state)
		{
		case 0:
		case 1:
		case 3:
		case 6:
		case 7:
			return 0;		

		case 2:
			return 1;	// integer
		case 4:
			return 2;
		case 5:
			buffer.append('0');
			return 2;
		case 8:
			return 3;	// float
		}

		return 0;
	}



	public static String nodeToString(com.altova.mapforce.IMFNode node) throws Exception
	{
		return node.value();
	}
	
	public static javax.xml.namespace.QName nodeToQName(com.altova.mapforce.IMFNode node) throws Exception
	{
		return castToQName(node);
	}

	public static com.altova.mapforce.IEnumerable castToEnumerable(Object o)
	{
		return (com.altova.mapforce.IEnumerable) o;
	}
	
	public static com.altova.functions.Core.Group castToGroup(Object o)
	{
		return (com.altova.functions.Core.Group) o;
	}
	
	public static com.altova.mapforce.IMFNode castToIMFNode(Object o)
	{
		if (o instanceof com.altova.mapforce.IMFNode)
			return (com.altova.mapforce.IMFNode) o;
		
		final class ANode implements com.altova.mapforce.IMFNode
		{
			String val;
			
			public ANode(String v) { val = v; }
		
			public int getNodeKind() { return com.altova.mapforce.IMFNode.MFNodeKind_Text; }
			public String getLocalName() { return ""; }
			public String getNamespaceURI() { return ""; }
			public String getPrefix() { return ""; }
            public String getNodeName() { return ""; }
			public javax.xml.namespace.QName getQName() { return new javax.xml.namespace.QName("", ""); }
			public com.altova.mapforce.IEnumerable select(int mfQueryKind, Object query) { return new com.altova.mapforce.MFEmptySequence(); }
			public String value() throws Exception { return val; }
			public javax.xml.namespace.QName qnameValue() { return null; }
			public Object typedValue() throws Exception {return val;}
		}
			
		return new ANode(o.toString());
	}









	
	public static String decimalToString(BigDecimal val)
	{
		return castToString(val);
	}
	
	public static double decimalToDouble(BigDecimal val)
	{
		return castToDouble(val);
	}

	public static BigDecimal parseDecimal(String s)
	{
		return castToBigDecimal(s);
	}
	
	public static String doubleToString(double d)
	{
		return castToString(d);
	}
	
	public static double parseDouble(String s)
	{
		return castToDouble(s);
	}

	public static String integerToString(BigInteger val)
	{
		return val.toString();
	}
	
	public static double integerToDouble(BigInteger val)
	{
		return castToDouble(val);
	}
	
	public static BigInteger longToInteger(long l)
	{
		return castToBigInteger(l);
	}
	
	public static BigInteger intToInteger(int i)
	{
		return castToBigInteger(i);
	}
	
	public static int integerToInt(BigInteger i)
	{
		return castToInt(i);
	}
	
	public static long integerToLong(BigInteger n)
	{
		return castToLong(n);
	}

	public static String DateTimeToString(DateTime dt)
	{
		return castToString(dt, DateTimeFormat_W3_dateTime);
	}
	
	public static DateTime parseDateTime(String s)
	{
		return castToDateTime(s);
	}
		
	public static String dateToString(DateTime dt)
	{
		return castToString(dt, DateTimeFormat_W3_date);
	}
	
	public static DateTime parseDate(String s)
	{
		return castToDateTime(s);
	}
	
	public static String timeToString(DateTime dt)
	{
		return castToString(dt, DateTimeFormat_W3_time);
	}
	
	public static DateTime parseTime(String s)
	{
		return castToDateTime(s);
	}
	
	public static String GYearToString(DateTime dt)
	{
		return castToString(dt, DateTimeFormat_W3_gYear);
	}
	
	public static DateTime parseGYear(String s)
	{
		return castToDateTime(s);
	}
	
	public static String GMonthToString(DateTime dt)
	{
		return castToString(dt, DateTimeFormat_W3_gMonth);
	}
	
	public static DateTime parseGMonth(String s)
	{
		return castToDateTime(s);
	}
	
	public static String GDayToString(DateTime dt)
	{
		return castToString(dt, DateTimeFormat_W3_gDay);
	}
	
	public static DateTime parseGDay(String s)
	{
		return castToDateTime(s);
	}
	
	public static String GYearMonthToString(DateTime dt)
	{
		return castToString(dt, DateTimeFormat_W3_gYearMonth);
	}
	
	public static DateTime parseGYearMonth(String s)
	{
		return castToDateTime(s);
	}
	
	public static String GMonthDayToString(DateTime dt)
	{
		return castToString(dt, DateTimeFormat_W3_gMonthDay);
	}
	
	public static DateTime parseGMonthDay(String s)
	{
		return castToDateTime(s);
	}
	
	public static String durationToString(Duration d)
	{
		return castToString(d);
	}

    public static Duration parseDuration(String s)
	{
		return castToDuration(s);
	}

	public static DateTime DateTimeToDateTimeStamp(DateTime dt)
	{
		if( dt.hasTimezone() == CalendarBase.TZ_MISSING )
		{
			dt.setHasTimezone(CalendarBase.TZ_UTC);
			dt.setTimezoneOffset(0);
		}
		return dt;
	}

	public static DateTime ParseDateTimeStamp(String s)
	{
		DateTime dt = castToDateTime(s);
		if( dt.hasTimezone() == CalendarBase.TZ_MISSING )
			throw new IllegalArgumentException("DateTimeStamp missing timezone information.");
		return dt;
	}

	public static String YearMonthDurationToString(Duration d)
	{
		return DurationToYearMonthDuration(d).toYearMonthString();
	}

	public static Duration DurationToYearMonthDuration(Duration d)
	{
		return new Duration( d.getYear(), d.getMonth(), 0, 0, 0, 0, 0.0, d.isNegative());
	}

	public static Duration DurationToDayTimeDuration(Duration d)
	{
		return new Duration( 0, 0, d.getDay(), d.getHour(), d.getMinute(), d.getSecond(), d.getPartSecond(), d.isNegative());
	}

	public static Duration parseYearMonthDuration(String s)
	{
		return castToDuration(s, Duration.ParseType.YEARMONTH);
	}
	
	public static Duration parseDayTimeDuration(String s)
	{
		return castToDuration(s, Duration.ParseType.DAYTIME);
	}
	
    public static String binaryToBase64String(byte[] b)
	{
		return Base64.encode(b);
	}
	
    public static byte[] parseBase64Binary(String s) throws Exception
	{
		return Base64.decode(s.replaceAll("\\s", ""));
	}
	
    public static String binaryToHexString(byte[] b)
	{
		return HexBinary.encode(b);
	}
	
    public static byte[] parseHexBinary(String s)
	{
		return HexBinary.decode(s);
	}
	
    public static String booleanToString(boolean b)
	{
		return castToString(b);
	}
	
    public static boolean parseBoolean(String s)
	{
		return castToBool(s);
	}
	
    public static String QNameToString(javax.xml.namespace.QName qn)
	{
		return castToString(qn);
	}
	
	public static javax.xml.namespace.QName stringToQName(String s)
	{
		return castToQName(s);
	}
	
	public static boolean stringToBoolean(String s)
	{
		return castToBool(s);
	}
	
	public static BigDecimal longToDecimal(long l)
	{
		return castToBigDecimal(l);
	}
	
	public static long intToLong(int i)
	{
		return castToLong(i);
	}
	
	public static BigDecimal intToDecimal(int i)
	{
		return castToBigDecimal(i);
	}
	
	public static int longToInt(long i)
	{
		return castToInt(i);
	}
	
	public static long decimalToLong(BigDecimal d)
	{
		return castToLong(d);
	}
	
	public static int decimalToInt(BigDecimal d)
	{
		return castToInt(d);
	}
	
	public static long parseLong(String s)
	{
		return castToLong(s);
	}
	
	public static BigInteger parseInteger(String s)
	{
		return castToBigInteger(s);
	}
	
	public static int parseInt(String s)
	{
		return castToInt(s);
	}
	
	public static BigDecimal integerToDecimal(BigInteger i) 
	{
		return castToBigDecimal(i);
	}
	
	public static BigInteger GYearToInteger(DateTime dt)
	{
		return BigInteger.valueOf((long)dt.getYear());
	}
	
	public static BigInteger decimalToInteger(BigDecimal d)
	{
		return castToBigInteger(d);
	}
	
	public static BigDecimal booleanToDecimal(boolean b)
	{
		return castToBigDecimal(b);
	}
	
	public static double booleanToDouble(boolean b)
	{
		return castToDouble(b);
	}
	
	public static long booleanToLong(boolean b)
	{
		return castToLong(b);
	}
	
	public static BigInteger booleanToInteger(boolean b)
	{
		return castToBigInteger(b);
	}
	
	public static int booleanToInt(boolean b)
	{
		return castToInt(b);
	}
	
	public static boolean decimalToBoolean(BigDecimal d)
	{
		return castToBool(d);
	}
	
	public static boolean doubleToBoolean(double d)
	{
		return castToBool(d);
	}
	
	public static boolean integerToBoolean(BigInteger i)
	{
		return castToBool(i);
	}
	
	public static BigDecimal doubleToDecimal(double d)
	{
		return castToBigDecimal(d);
	}
	
	public static BigInteger doubleToInteger(double d)
	{
		return castToBigInteger(d);
	}
	
	
	
	
	
	
	
	public static int castToInt(boolean b)
	{
		return b ? 1 : 0;
	}
	
	public static int castToInt(Boolean b)
	{
		return castToInt(b.booleanValue());
	}

	public static int castToInt(int i)
	{
		return i;
	}
	
	public static int castToInt(Integer i)
	{
		return i.intValue();
	}
	

	public static int castToInt(long i)
	{
		if (i < (long) Integer.MIN_VALUE || i > (long) Integer.MAX_VALUE)
			throw new ArithmeticException("Numeric value overflow");
		return (int) i;
	}
	
	public static int castToInt(Long i)
	{
		return castToInt(i.longValue());
	}
	

	public static int castToInt(double d)
	{
		if (d < (double) Integer.MIN_VALUE || d > (double) Integer.MAX_VALUE)
			throw new ArithmeticException("Numeric value overflow");
		return (int) d;
	}
	
	public static int castToInt(Double d)
	{
		return castToInt(d.doubleValue());
	}

	public static int castToInt(BigInteger n)
	{
		if (n.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0)
			throw new ArithmeticException("Numeric value overflow");
		return n.intValue();
	}

	public static int castToInt(BigDecimal n)
	{
		if (n.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0)
			throw new ArithmeticException("Numeric value overflow");
		return n.intValue();
	}

	public static int castToInt(String s)
	{
		if (s == null)
			return 0;

		if (s.equals("INF") || s.equals("-INF") || s.equals("NaN"))
			throw new ArithmeticException("'" + s + "' is too large for int.");

		s = s.trim();
		java.lang.StringBuffer buf = new java.lang.StringBuffer();
		switch (prepareNumber(buf, s))
		{
			case 1:
				return Integer.parseInt(buf.toString());
			case 2:
				return new BigDecimal(buf.toString()).intValue();
			case 3:
				return (int)Double.parseDouble(buf.toString());
			default:
				throw new NumberFormatException("'" + s + "' cannot be converted to int.");
		}
	}


	public static long castToLong(boolean b)
	{
		return b ? 1 : 0;
	}

	public static long castToLong(Boolean b)
	{
		return b.booleanValue() ? 1 : 0;
	}
	
	public static long castToLong(int i)
	{
		return (long) i;
	}
	
	public static long castToLong(Integer i)
	{
		return (long) i.intValue();
	}
	
	public static long castToLong(long i)
	{
		return i;
	}
	
	public static long castToLong(Long i)
	{
		return i.longValue();
	}

	public static long castToLong(double d)
	{
		if (d < (double) Long.MIN_VALUE || d > (double) Long.MAX_VALUE)
			 throw new ArithmeticException("Numeric value overflow");
		 return (long) d;
	}
	
	public static long castToLong(Double d)
	{
		return castToLong(d.doubleValue());
	}

	public static long castToLong(BigInteger n)
	{
		if (n.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0)
			throw new ArithmeticException("Numeric value overflow");
		return n.longValue();
	}

	public static long castToLong(BigDecimal n)
	{
		if (n.compareTo(BigDecimal.valueOf(Long.MAX_VALUE)) > 0)
			throw new ArithmeticException("Numeric value overflow");
		return n.longValue();
	}

	public static long castToLong(String s)
	{
		if (s == null)
			return 0;

		if (s.equals("INF") || s.equals("-INF") || s.equals("NaN"))
			throw new ArithmeticException("'" + s + "' is too large for long.");

		s = s.trim();
		java.lang.StringBuffer buf = new java.lang.StringBuffer();
		switch (prepareNumber(buf, s))
		{
			case 1:
				return Long.parseLong(buf.toString());
			case 2:
				return new BigDecimal(buf.toString()).longValue();
			case 3:
				return (long)Double.parseDouble(buf.toString());
			default:
				throw new NumberFormatException("'" + s + "' cannot be converted to long.");
		}
	}
	

	public static BigInteger castToBigInteger(boolean b)
	{
		return BigInteger.valueOf( b ? 1 : 0 );
	}
	
	public static BigInteger castToBigInteger(Boolean b)
	{
		return BigInteger.valueOf( b.booleanValue() ? 1 : 0 );
	}

	public static BigInteger castToBigInteger(int i)
	{
		return BigInteger.valueOf( i );
	}
	
	public static BigInteger castToBigInteger(Integer i)
	{
		return BigInteger.valueOf( i.intValue() );
	}

	public static BigInteger castToBigInteger(long i)
	{
		return BigInteger.valueOf( i );
	}
	
	public static BigInteger castToBigInteger(Long i)
	{
		return BigInteger.valueOf( i.longValue() );
	}

	public static BigInteger castToBigInteger(double d)
	{
		BigDecimal deci = new BigDecimal( d );
		return deci.toBigInteger();
	}
	
	public static BigInteger castToBigInteger(Double d)
	{
		return castToBigInteger(d.doubleValue());
	}

	public static BigInteger castToBigInteger(BigInteger n)
	{
		return n;
	}

	public static BigInteger castToBigInteger(BigDecimal n)
	{
		return n.toBigInteger();
	}

	public static BigInteger castToBigInteger(String s)
	{
		if (s == null)
			return null;

		if (s.equals("INF") || s.equals("-INF") || s.equals("NaN"))
			throw new ArithmeticException("'" + s + "' is too large for integer.");

		s = s.trim();
		java.lang.StringBuffer buf = new java.lang.StringBuffer();
		switch (prepareNumber(buf, s))
		{
			case 1:
				return new BigInteger(buf.toString());
			case 2:
			case 3:
				return new BigDecimal(buf.toString()).toBigInteger();
			default:
				throw new NumberFormatException("'" + s + "' cannot be converted to integer.");
		}
	}
	

	public static BigDecimal castToBigDecimal(boolean b)
	{
		return BigDecimal.valueOf( b ? 1 : 0 );
	}
	
	public static BigDecimal castToBigDecimal(Boolean b)
	{
		return BigDecimal.valueOf( b.booleanValue() ? 1 : 0 );
	}

	public static BigDecimal castToBigDecimal(int i)
	{
		return BigDecimal.valueOf( i );
	}
	
	public static BigDecimal castToBigDecimal(Integer i)
	{
		return BigDecimal.valueOf( i.intValue() );
	}

	public static BigDecimal castToBigDecimal(long i)
	{
		return BigDecimal.valueOf( i );
	}
	
	public static BigDecimal castToBigDecimal(Long i)
	{
		return BigDecimal.valueOf( i.longValue() );
	}

	public static BigDecimal castToBigDecimal(double d)
	{
		int scale = 14;
		double dAbs = (double) Math.abs((float) d);
		if( dAbs > 0.0 )
		{
			int nStartDigit = (int) (Math.log(dAbs) / Math.log(10.0));
			scale = 14 - nStartDigit;
			if( scale < 0 ) scale = 0;
		}
		return new BigDecimal( d ).setScale(scale, BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal castToBigDecimal(Double d)
	{
		return castToBigDecimal( d.doubleValue() );
	}

	public static BigDecimal castToBigDecimal(BigInteger n)
	{
		return new BigDecimal( n );
	}

	public static BigDecimal castToBigDecimal(BigDecimal n)
	{
		return n;
	}

	public static BigDecimal castToBigDecimal(String s)
	{
		if (s == null)
			return null;

		if (s.equals("INF") || s.equals("-INF") || s.equals("NaN"))
			throw new ArithmeticException("'" + s + "' is too large for decimal.");

		s = s.trim();
		java.lang.StringBuffer buf = new java.lang.StringBuffer();
		switch (prepareNumber(buf, s))
		{
			case 1:
			case 2:
			case 3:
				return new BigDecimal(buf.toString());
			default:
				throw new NumberFormatException("'" + s + "' cannot be converted to decimal.");
		}
	}
	

	public static double castToDouble(boolean b)
	{
		return b ? 1.0 : 0.0;
	}
	
	public static double castToDouble(Boolean b)
	{
		return b.booleanValue() ? 1.0 : 0.0;
	}
	
	public static double castToDouble(int i)
	{
		return (double) i;
	}
	
	public static double castToDouble(Integer i)
	{
		return (double) i.intValue();
	}

	public static double castToDouble(long i)
	{
		return (double) i;
	}
	
	public static double castToDouble(Long i)
	{
		return (double) i.longValue();
	}

	public static double castToDouble(double d)
	{
		return d;
	}
	
	public static double castToDouble(Double d)
	{
		return d.doubleValue();
	}

	public static double castToDouble(BigInteger n)
	{
		BigDecimal dec = new BigDecimal( n );
		if( dec.compareTo( new BigDecimal(Double.MAX_VALUE) ) > 0 )
			throw new ArithmeticException("Numeric value overflow");
		return dec.doubleValue();
	}

	public static double castToDouble(BigDecimal n)
	{
		if( n.compareTo( new BigDecimal(Double.MAX_VALUE) ) > 0 )
			throw new ArithmeticException("Numeric value overflow");
		return n.doubleValue();
	}

	public static double castToDouble(String s)
	{
		if (s == null)
			return 0;

		if (s.equals("INF"))
			return Double.POSITIVE_INFINITY;
		else if (s.equals("-INF"))
			return Double.NEGATIVE_INFINITY;
		else if (s.equals("NaN"))
			return Double.NaN;

		s = s.trim();
		java.lang.StringBuffer buf = new java.lang.StringBuffer();
		switch (prepareNumber(buf, s))
		{
			case 1:
			case 2:
			case 3:
				return Double.parseDouble(buf.toString());
			default:
				throw new NumberFormatException("'" + s + "' cannot be converted to double.");
		}
	}


	public static String castToString(int i)
	{
		return Integer.toString(i);
	}
	
	public static String castToString(Integer i)
	{
		return i.toString();
	}

	public static String castToString(long i)
	{
		return Long.toString(i);
	}
	
	public static String castToString(Long i)
	{
		return i.toString();
	}

	public static String castToString(double d)
	{
		double dAbs = Math.abs(d);
		DecimalFormatSymbols decfmtsymb = new DecimalFormatSymbols(java.util.Locale.US);	// US locale represents numbers with a . as comma and without any (ten)thousend separators.
		decfmtsymb.setInfinity("INF");
		decfmtsymb.setNaN("NaN");
		DecimalFormat decfmt = new DecimalFormat((dAbs > 1E7 || dAbs < 1E-3) && dAbs > 0.0 ?
				"0.##############E000" : "0.#############", decfmtsymb);
		
		String s = decfmt.format(d);
		// 1.234E567 => 1.234E+567
		int e = s.indexOf("E");
		if (e > 0 && s.charAt(e+1) != '-' )
		s = s.substring(0, e) + "E+" + s.substring(e+1);
		return s;
	}
	
	public static String castToString(Double d)
	{
		return castToString(d.doubleValue());
	}

	public static String castToString(BigInteger val)
	{
		return val.toString();
	}

	public static String castToString(BigDecimal val)
	{
		int sign = val.signum();
		val = val.abs();
		String s = val.unscaledValue().toString();
		while (s.length() <= val.scale()) s = "0" + s;
		while (s.length() < -val.scale()) s = s + "0";
		if (val.scale() > 0) {
		s = s.substring(0, s.length() - val.scale()) + "." + s.substring(s.length() - val.scale(), s.length());
		   while (s.endsWith("0")) s = s.substring(0, s.length() - 1);
		   if (s.endsWith(".")) s = s.substring(0, s.length() - 1);
		}
		if (sign < 0) s = "-" + s;
		return s;
	}

	public static String castToString(String s)
	{
		return s;
	}

	public static String castToString(boolean b)
	{
		if (b) 
			return "true";
		return "false";
	}
	
	public static String castToString(Boolean b)
	{
		return castToString(b.booleanValue());
	}

	public static String castToString(byte[] b)
	{
		if( b == null ) return "";
		String sResult = com.altova.Base64.encode( b);
		return sResult.replaceAll( "\r", "" );
	}

	public static String castToString(DateTime dt)
	{
		return dt.toString();
	}

	public static String castToString(DateTime dt, int format)
	{
		if( format == DateTimeFormat_W3_dateTime )		/* Format as schema dateTime */
		{
			return dt.toString();
		}
		if( format == DateTimeFormat_W3_date )			/* Format as schema date */
		{
			return dt.toDateString(true);
		}
		if( format == DateTimeFormat_W3_time )			/* Format as schema time */
		{
			return dt.toTimeString();
		}
		if( format == DateTimeFormat_W3_gYear )		/* Format as schema gYear */
		{
			String result = "";
			int year = dt.getYear();
			if (year < 0)
			{
				result += '-';
				year = -year;
			}

			result += formatNumber(year, 4);
			if (dt.hasTimezone() != CalendarBase.TZ_MISSING)
				result = formatTimezone(dt.getTimezoneOffset());
			return result;
		}
		if( format == DateTimeFormat_W3_gYearMonth )	/* Format as schema gYearMonth */
		{
			String result = "";
			int year = dt.getYear();
			int month = dt.getMonth();
			if (year < 0)
			{
				result += '-';
				year = -year;
			}

			result += formatNumber(year, 4);
			result += '-';
			result += formatNumber(month, 2);
			if (dt.hasTimezone() != CalendarBase.TZ_MISSING)
				result += formatTimezone(dt.getTimezoneOffset());
			return result;
		}
		if( format == DateTimeFormat_W3_gMonth )		/* Format as schema gMonth */
		{
			String result = "--";
			int month = dt.getMonth();
			result += formatNumber(month, 2);
			if (dt.hasTimezone() != CalendarBase.TZ_MISSING)
				formatTimezone(dt.getTimezoneOffset());
			return result;
		}
		if( format == DateTimeFormat_W3_gMonthDay )	/* Format as schema gMonthDay */
		{
			String result = "--";
			int month = dt.getMonth();
			int day = dt.getDay();
			result += formatNumber(month, 2);
			result += '-';
			result += formatNumber(day, 2);
			if (dt.hasTimezone() != CalendarBase.TZ_MISSING)
				result += formatTimezone(dt.getTimezoneOffset());
			return result;
		}
		if( format == DateTimeFormat_W3_gDay )			/* Format as schema gDay */
		{
			String result = "---";
			int day = dt.getDay();
			result += formatNumber(day, 2);
			if (dt.hasTimezone() != CalendarBase.TZ_MISSING)
				result += formatTimezone(dt.getTimezoneOffset());
			return result;
		}
		if( format == DateTimeFormat_S_DateTime )		/* Format as standard DateTime "YYYY-MM-DD HH:MM:SS" */
		{
			StringBuffer s = new StringBuffer();
			s.append( dt.toDateString() );
			s.append(" ");
			s.append( dt.toTimeString() );
			return s.toString();
		}
		if( format == DateTimeFormat_S_Seconds )		/* Format as number of seconds since epoch */
		{
			String result = "";
			long milliseconds = dt.getTimeValue();
			if (milliseconds < 0)
			{
				result += '-';
				milliseconds = -milliseconds;
			}
			result += formatNumber(milliseconds / 1000, 1);
			result += formatFraction(milliseconds % 1000, 7);
			if (dt.hasTimezone() != CalendarBase.TZ_MISSING)
				result += formatTimezone(dt.getTimezoneOffset());
			return result;
		}
		if( format == DateTimeFormat_S_Days )			/* Format as number of days since epoch */
		{
			String result = "";
			long milliseconds = dt.getTimeValue();
			if (milliseconds < 0)
			{
				result += '-';
				milliseconds = -milliseconds;
			}
			result += formatNumber(milliseconds / (86400 * 1000), 1);
			result += formatFraction(milliseconds % (86400 * 1000), 7);
			if (dt.hasTimezone() != CalendarBase.TZ_MISSING)
				result += formatTimezone(dt.getTimezoneOffset());
			return result;
		}

		throw new IllegalArgumentException("Unsupported datetime format.");
	}

	public static String castToString(Duration dur)
	{
		return dur.toString();
	}
	
	public static String castToString(javax.xml.namespace.QName qn)
	{
		return qn.getPrefix().equals(javax.xml.XMLConstants.DEFAULT_NS_PREFIX) ? qn.getLocalPart() : qn.getPrefix() + ":" + qn.getLocalPart();
	}

	public static DateTime castToDateTime(String s)
	{
		if (s == null)
			throw new java.lang.NullPointerException();
		if (s.length() == 0)
			throw new IllegalArgumentException("Cast to DateTime failed.");
		return DateTime.parse( s );
	}

	public static Duration castToDuration(String s)
	{
		return castToDuration(s, Duration.ParseType.DURATION);
	}

	public static Duration castToDuration(String s, Duration.ParseType pt)
	{
		if (s == null)
			throw new java.lang.NullPointerException();
		if (s.length() == 0)
			throw new IllegalArgumentException("Cast to Duration failed.");
		return Duration.parse( s, pt );
	}

	public static DateTime castToDateTime(DateTime dt)
	{
		return dt;
	}

	public static DateTime castToDateTime(DateTime dt, int format)
	{
		return dt;
	}

	public static Duration castToDuration(Duration dur)
	{
		return dur;
	}

	public static boolean castToBool(boolean b)
	{
		return b;
	}
	
	public static boolean castToBool(Boolean b)
	{
		return b.booleanValue();
	}

	public static boolean castToBool(int i)
	{
		return i != 0;
	}
	
	public static boolean castToBool(Integer i)
	{
		return i.intValue() != 0;
	}

	public static boolean castToBool(long i)
	{
		return i != 0;
	}
	
	public static boolean castToBool(Long i)
	{
		return i.longValue() != 0;
	}

	public static boolean castToBool(double d)
	{
		return d != 0;
	}
	
	public static boolean castToBool(Double d)
	{
		return d.doubleValue() != 0;
	}

	public static boolean castToBool(BigInteger n)
	{
		return n.compareTo(BigInteger.ZERO) != 0;
	}

	public static boolean castToBool(BigDecimal n)
	{
		return n.compareTo(new BigDecimal (0.0)) != 0;
	}

	public static boolean castToBool(String v)
	{
		String s = v.trim();
		if (s.equals("true") || s.equals("1")) return true;
		if (s.equals("false") || s.equals("0")) return false;
		throw new IllegalArgumentException("'" + v + "' cannot be parsed as boolean.");	
	}

	public static boolean exists(Object o)
	{
		return o != null;
	}

	public static int castToInt(Object v)
	{
		if (!exists(v))
			throw new java.lang.NullPointerException();
		if (v instanceof Integer)
			return ((Integer)v).intValue();
		return castToInt(v.toString());
	}

	public static long castToLong(Object v)
	{
		if (!exists(v))
			throw new java.lang.NullPointerException();
		if (v instanceof Long)
			return ((Long)v).longValue();
		return castToLong(v.toString());
	}

	public static BigInteger castToBigInteger(Object v)
	{
		if (!exists(v))
			throw new java.lang.NullPointerException();
		if (v instanceof BigInteger)
			return (BigInteger)v;
		return castToBigInteger(v.toString());
	}

	public static BigDecimal castToBigDecimal(Object v)
	{
		if (!exists(v))
			throw new java.lang.NullPointerException();
		if (v instanceof BigDecimal)
			return (BigDecimal)v;
		return castToBigDecimal(v.toString());
	}

	public static double castToDouble(Object v)
	{
		if (!exists(v))
			throw new java.lang.NullPointerException();
		if (v instanceof Double)
			return ((Double)v).doubleValue();
		return castToDouble(v.toString());
	}

	public static String castToString(Object v) throws Exception
	{
		if (!exists(v))
			throw new java.lang.NullPointerException();
		if (v instanceof String)
			return (String)v;
		if (v instanceof java.math.BigDecimal)
			return castToString((java.math.BigDecimal)v);
		if (v instanceof Double)
			return castToString(((Double)v).doubleValue());
		if (v instanceof Float)
			return castToString((double) (((Float)v).floatValue()));
		if (v instanceof javax.xml.namespace.QName)
			return castToString((javax.xml.namespace.QName)v);
		if (v instanceof Boolean)
			return castToString(((Boolean)v).booleanValue());
		if (v instanceof byte[])
			return castToString((byte[])v);

		if (v instanceof com.altova.mapforce.IMFNode)
			return ((com.altova.mapforce.IMFNode) v).value();

		return v.toString();
	}

	public static DateTime castToDateTime(Object v)
	{
		if (!exists(v))
			throw new java.lang.NullPointerException();
		return (DateTime)v;
	}

	public static Duration castToDuration(Object v)
	{
		if (!exists(v))
			throw new java.lang.NullPointerException();
		return (Duration)v;
	}

	public static boolean castToBool(Object v)
	{
		if (!exists(v))
			throw new java.lang.NullPointerException();
		if (v instanceof Boolean)
			return ((Boolean)v).booleanValue();
		return castToBool(v.toString());
	}
	
	public static byte[] castToBinary(byte[] b) 
	{
		return b;
	}
	
	public static byte[] castToBinary(Object v)
	{
		if (!exists(v))
			throw new java.lang.NullPointerException();
		
		return (byte[])v;
	}

	public static javax.xml.namespace.QName castToQName(com.altova.mapforce.IMFNode n) throws Exception
	{
		return n.qnameValue();
	}


	public static javax.xml.namespace.QName castToQName(Object o) throws Exception
	{
		return (javax.xml.namespace.QName) o;
	}

	public static javax.xml.namespace.QName castToQName(String s)
	{
		int i = s.indexOf('{');
		int j = s.indexOf('}');
		if (i==0 && j>i)
			return new javax.xml.namespace.QName(s.substring(1, j), s.substring(j+1));
		
		return new javax.xml.namespace.QName(s);
	}
	
	public static javax.xml.namespace.QName castToQName(javax.xml.namespace.QName qn)
	{
		return qn;
	}
	
	public static String formatNumber(long value, long minDigits)
	{
		DecimalFormat fmt = new DecimalFormat("0", new DecimalFormatSymbols(java.util.Locale.US));
		fmt.setMinimumIntegerDigits( (int) minDigits );
		return fmt.format( value );
	}

	public static String formatTimezone(int value)
	{
		String result = "";
		
		if (value == 0)
			result += 'Z';
		else
		{
			if (value < 0)
			{
				result += '-';
				value = -value;
			}
			else
			{
				result += '+';
			}
			result += formatNumber(value / 60, 2);
			result += ':';
			result += formatNumber(value % 60, 2);
		}
		return result;
	}

	public static String formatFraction(long value, long precision)
	{
		String result = "";
		if (value != 0)
		{
			result += '.';
			result += formatNumber(value, precision);
			int i = result.length();
			while (result.charAt(i - 1) == '0')
				i -= 1;
			result = result.substring( 0, i );
		}
		return result;
	}

	public static java.lang.Boolean box( boolean    v ) { return new java.lang.Boolean(v); }
	public static java.lang.Byte    box( byte       v ) { return new java.lang.Byte   (v); }
	public static java.lang.Short   box( short      v ) { return new java.lang.Short  (v); }
	public static java.lang.Integer box( int        v ) { return new java.lang.Integer(v); }
	public static java.lang.Long    box( long       v ) { return new java.lang.Long   (v); }
	public static java.lang.Float   box( float      v ) { return new java.lang.Float  (v); }
	public static java.lang.Double  box( double     v ) { return new java.lang.Double (v); }
	public static Object            box( Object     v ) { return v; }
	public static String            box( String     v ) { return v; }
	public static byte[]          box( byte[]   v ) { return v; }
	public static BigDecimal        box( BigDecimal v ) { return v; }
	public static BigInteger        box( BigInteger v ) { return v; }
	public static DateTime          box( DateTime   v ) { return v; }
	public static Duration          box( Duration   v ) { return v; }
}
