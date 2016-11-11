////////////////////////////////////////////////////////////////////////
//
// EDIDateTimeHelpers.java
//
// This file was generated by MapForce 2015r4sp1.
//
// YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
// OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
//
// Refer to the MapForce Documentation for further details.
// http://www.altova.com/mapforce
//
////////////////////////////////////////////////////////////////////////

package com.altova.text.edi;
import com.altova.CoreTypes;

public class EDIDateTimeHelpers
{
	public static boolean IsYearCorrect( String sRhs )
	{
		for (int i=0; i<sRhs.length(); ++i)
			if ( !Character.isDigit( sRhs.charAt(i) ) ) return false;

		return true;
	}

	public static boolean IsMonthCorrect( String sRhs )
	{
		try
		{
			long nMonth = CoreTypes.castToInt( sRhs );
			if ( ( 1 > nMonth ) || ( 12 < nMonth ) ) return false;
				return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static boolean IsDayCorrect( String sRhs )
	{
		try
		{
			long nDay = CoreTypes.castToInt( sRhs );
			if ( ( 1 > nDay ) || ( 31 < nDay ) ) return false;
				return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static boolean IsHourCorrect( String sRhs )
	{
		try
		{
			long nHour = CoreTypes.castToInt( sRhs );
			if ( ( 0 > nHour ) || ( 23 < nHour ) ) return false;
				return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static boolean IsMinuteCorrect( String sRhs )
	{
		try
		{
			long nMinute = CoreTypes.castToInt( sRhs );
			if ( ( 0 > nMinute ) || ( 59 < nMinute ) ) return false;
				return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static boolean IsDateCorrect( String sRhs )
	{
		// year can be YYYYMMDD or YYMMDD
		int yLen = 2;
		if ( sRhs.length() == 8)
			yLen = 4;
		else if (sRhs.length() == 6)
			yLen = 2;
		else if (sRhs.length() == 5) // special case for 0YMMDD where zero was removed because it is decimal type
			yLen = 1;
		else
			return false; // bad length

		if ( !IsYearCorrect( sRhs.substring( 0, yLen ) ) ) return false;
		if (!IsMonthCorrect(sRhs.substring(yLen, yLen + 2))) return false;
		if (!IsDayCorrect(sRhs.substring(sRhs.length() - 2))) return false;
		return true;
	}

	public static boolean IsTimeCorrect( String sRhs )
	{
		for( int i = 0; i < sRhs.length(); i++)
		{
			if( !Character.isDigit(sRhs.charAt(i)))
				return false;
		}

		long len = sRhs.length();

		// Time is HHMM[SSd...d]
		if ( !IsHourCorrect( sRhs.substring( 0, 2 ) ) ) return false;
		if (!IsMinuteCorrect(sRhs.substring(2, 4))) return false;
		if ( len > 4 ) // have secs?
			if (len < 6 || !IsMinuteCorrect(sRhs.substring(4, 6))) return false; // seconds same as minutes 0..59

		return true;
	}
}
