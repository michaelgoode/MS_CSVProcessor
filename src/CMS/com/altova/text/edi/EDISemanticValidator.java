////////////////////////////////////////////////////////////////////////
//
// EDISemanticValidator.java
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

import java.util.HashMap;

import com.altova.CoreTypes;

public class EDISemanticValidator
{
	private long mnSegmentCount;
	private long mnGroupCount;
	private long mnMessageCount;
	private EDISettings mSettings;
	HashMap<String, String> mMapEquality =
		new HashMap<String, String>();
	String msCurrentSegment;
	private String msCurrentMessageType;

	public EDISemanticValidator(EDISettings settings)
	{
		mSettings = settings;
		mnSegmentCount = 0;
		mnGroupCount = 0;
		mnMessageCount = 0;
	}

	public String validate ( String sField, String sValue )
	{
		switch( mSettings.getStandard())
		{
		case EDIFACT: return validateEDIFACT( sField, sValue );
		case EDIX12: return validateX12( sField, sValue );
		case EDIHL7: return validateHL7( sField, sValue );
		case EDITRADACOMS: return validateTRADACOMS( sField, sValue );
		}

		return "";
	}

	public void segment( String sSegment )
	{
		switch( mSettings.getStandard())
		{
		case EDIFACT:
			if ( sSegment.equals("UNB") )
			{
				mnGroupCount = 0;
			}
			else if ( sSegment.equals("UNG") )
			{
				mnGroupCount++;
				mnMessageCount = 0;
			}
			else if ( sSegment.equals("UNH") )
			{
				mnMessageCount++;
				mnSegmentCount = 1;
			}
			else
			{
				mnSegmentCount++;
			}
			break;
		case EDIX12:
			if ( sSegment.equals("ISA") )
			{
				mnGroupCount = 0;
			}
			else if ( sSegment.equals("GS") )
			{
				mnGroupCount++;
				mnMessageCount = 0;
			}
			else if ( sSegment.equals("ST") )
			{
				mnMessageCount++;
				mnSegmentCount = 1;
			}
			else
			{
				mnSegmentCount++;
			}
			break;
		case EDITRADACOMS:
			if ( sSegment.equals("STX") )
			{
				mnMessageCount = 0;
			}
			else if (sSegment.equals("BAT") )
			{
				mnGroupCount = 0; // count messages in batch
			}
			else if ( sSegment.equals("MHD") )
			{
				mnMessageCount++;
				mnGroupCount++;
				mnSegmentCount = 1;
			}
			else
			{
				mnSegmentCount++;
			}
			break;
		}

		msCurrentSegment = sSegment;
	}

	private String validateX12( String sField, String sValue )
	{
		StringBuffer sError = new StringBuffer();
		String sExpectedValue;

		if ( sField.equals("F96") ) // Segment count in group
		{
			long nValue = CoreTypes.castToInt( sValue );
			if ( nValue != mnSegmentCount )
			{
				sError.append( "Field does not contain the correct number of segments (field value = ");
				sError.append( sValue);
                sError.append( ", counted ");
                sError.append( mnSegmentCount);
                sError.append( " ).");
			}
		}

		else if ( sField.equals( "F97") ) // Message count in group
		{
			if ( msCurrentSegment.equals("GE") )
			{
				long nValue = CoreTypes.castToInt( sValue );
				if ( nValue != mnMessageCount )
				{
					sError.append( "Field does not contain the correct number of messages (field value = ");
					sError.append( sValue);
                    sError.append( ", counted ");
                    sError.append(mnMessageCount);
                    sError.append( " ).");
				}
			}
		}

		else if ( sField.equals("F28") )
		{
			if ( msCurrentSegment.equals("GS") || msCurrentSegment.equals("GE") )
			{
				sExpectedValue = validateEquality( sField, sValue );
				if ( !sExpectedValue.equals("") )
				{
					sError.append("Field does not contain the same value as Interchange/Group/GS/F28 ('");
					sError.append( sValue);
					sError.append("' instead of '");
					sError.append(sExpectedValue);
					sError.append("').");
				}
			}
		}

		else if ( sField.equals("F329") )
		{
			if ( msCurrentSegment.equals("ST") || msCurrentSegment.equals("SE") )
			{
				sExpectedValue = validateEquality( sField, sValue );
				if ( !sExpectedValue.equals("") )
                {
					sError.append( "Field does not contain the same value as Interchange/Group/Message/ST/F329 ('");
					sError.append( sValue);
					sError.append( "' instead of '");
					sError.append( sExpectedValue);
					sError.append("').");
                }
			}
		}

		else if ( sField.equals( "F143") ) // message type
		{
			if ( msCurrentSegment.equals("ST") )
				if ( msCurrentMessageType != null && !msCurrentMessageType.substring(0, 3).equals( sValue ) )
				{
					sError.append("'");
					sError.append(sValue);
					sError.append("' is not a correct message type specifier.");
				}
		}


		else if ( sField.equals("FI16") ) // Group count in interchange
		{
	        long nValue = CoreTypes.castToInt( sValue );
			if ( nValue != mnGroupCount )
			{
                sError.append("Field does not contain the correct number of function groups (field value = ");
                sError.append( sValue);
                sError.append(", counted ");
                sError.append( mnGroupCount);
                sError.append(" ).");
			}
		}

		else if ( sField.equals("FI12") && ( msCurrentSegment.equals("ISA") || msCurrentSegment.equals("IEA") ) )
		{
			sExpectedValue = validateEquality( sField, sValue );
			if ( !sExpectedValue.equals("") )
			{
				sError.append("Field does not contain the same value as Interchange/Group/Message/ISA/FI12 ('");
				sError.append("' instead of '");
				sError.append( sExpectedValue);
				sError.append("').");
			}
		}

		return sError.toString();
	}

	private String validateEDIFACT( String sField, String sValue )
	{
		StringBuffer sError = new StringBuffer();
		String sExpectedValue;

        if ( sField.equals("F0074") && msCurrentSegment.equals( "UNT") ) // segment count in message
        {
            long nValue = CoreTypes.castToInt( sValue );
	        if ( nValue != mnSegmentCount )
	        {
		        sError.append("Field does not contain the correct number of segments (field value = ");
				sError.append( sValue);
		        sError.append(", counted ");
		        sError.append( mnSegmentCount);
		        sError.append(" ).");
	        }
        }

        if ( sField.equals("F0060") && msCurrentSegment.equals( "UNE") ) // message count in group
        {
            long nValue = CoreTypes.castToInt( sValue );
	        if ( nValue != mnMessageCount )
	        {
		        sError.append( "Field does not contain the correct number of messages (field value = ");
		        sError.append( sValue);
		        sError.append(", counted ");
		        sError.append( mnMessageCount);
		        sError.append(" ).");
	        }
        }

        if ( sField.equals("F0036") && msCurrentSegment.equals( "UNZ") ) // message or group count in interchange
        {
	        // if we have at least one group, we count groups, otherwise messages
	        if ( mnGroupCount > 0) // count groups
	        {
                long nValue = CoreTypes.castToInt( sValue );
		        if ( nValue != mnGroupCount )
		        {
			        sError.append( "Field does not contain the correct number of function group (field value = ");
			        sError.append( sValue);
			        sError.append(", counted ");
			        sError.append( mnGroupCount);
			        sError.append(" ).");
		        }
	        }
	        else // count messages
	        {
                long nValue = CoreTypes.castToInt( sValue );
		        if ( nValue != mnMessageCount )
		        {
			        sError.append( "Field does not contain the correct number of messages (field value = ");
			        sError.append( sValue);
			        sError.append( ", counted ");
			        sError.append( mnMessageCount);
			        sError.append(" ).");
		        }
	        }
        }

        else if ( sField.equals("F0062") && ( msCurrentSegment.equals("UNH") || msCurrentSegment.equals( "UNT") ) ) // message reference number
        {
	        sExpectedValue = validateEquality( sField, sValue );
	        if ( !sExpectedValue.equals( "") )
	        {
		        sError.append( "Field does not contain the same value as Interchange/Group/Message/UNH/F0062 ('");
		        sError.append( sValue);
		        sError.append( "' instead of '");
		        sError.append( sExpectedValue);
		        sError.append( "').");
	        }
        }

        else if ( sField.equals("F0020") & ( msCurrentSegment.equals("UNZ") || msCurrentSegment.equals( "UNB") ) ) // interchange reference number
        {
	        sExpectedValue = validateEquality( sField, sValue );
	        if ( !sExpectedValue.equals( "") )
	        {
                sError.append( "Field does not contain the same value as Interchange/UNB/F0020 ('");
                sError.append( sValue);
                sError.append( "' instead of '");
                sError.append( sExpectedValue);
                sError.append( "').");
	        }
        }

        else if ( sField.equals("F0048") ) // group reference number
        {
	        sExpectedValue = validateEquality( sField, sValue );
	        if ( !sExpectedValue.equals( "") )
	        {
		        sError.append( "Field does not contain the same value as Interchange/Group/UNG/F0048 ('");
		        sError.append( sValue);
		        sError.append( "' instead of '");
		        sError.append( sExpectedValue);
		        sError.append( "').");
	        }
        }

        else if ( sField.equals("F0052") ) // version
        {
	        if (!sValue.equals(mSettings.getVersion()) )
	        {
		        sError.append( "Field value '");
		        sError.append( sValue);
		        sError.append( "' does not match expected message version number ('");
		        sError.append( mSettings.getVersion());
		        sError.append( "')");
	        }
        }

        else if ( sField.equals("F0054") ) // release
        {
	        if (!sValue.equals(mSettings.getRelease()) )
	        {
		        sError.append( "Field value '");
		        sError.append( sValue);
		        sError.append( "' does not match expected release number ('");
		        sError.append( mSettings.getRelease());
		        sError.append( "')");
	        }
        }

/*        else if ( sField.equals("F0051") ) // agency
        {
	        if (!sValue.equals(mSettings.getControllingAgency()) )
	        {
		        sError.append( "Field value '");
		        sError.append( sValue);
		        sError.append( "' does not match expected control agency code ('");
		        sError.append( mSettings.getControllingAgency());
		        sError.append( "')");
	        }
        }
*/
        else if ( sField.equals("F0065") && msCurrentSegment.equals( "UNH") ) // message type
        {
    		if ( msCurrentMessageType != null && !msCurrentMessageType.equals( sValue ) )
		    {
	        	sError.append( "'");
	        	sError.append( sValue);
	        	sError.append("' is not a correct message type specifier.");
		    }
        }

        else if ( sField.equals("F0017") ) // date
        {
	        if ( !EDIDateTimeHelpers.IsDateCorrect( sValue ) )
	        {
		        sError.append( "'");
		        sError.append( sValue);
		        sError.append( "' is not an EDI formatted date value.");
	        }
        }

        else if ( sField.equals("F0019") ) // time
        {
	        if ( !EDIDateTimeHelpers.IsTimeCorrect( sValue ) )
	        {
		        sError.append( "'");
		        sError.append( sValue);
		        sError.append( "' is not an EDI formatted time value.");
	        }
        }

        return sError.toString();
	}

	private String validateTRADACOMS( String sField, String sValue )
	{
		StringBuffer sError = new StringBuffer();
		String sExpectedValue;

		if ( msCurrentSegment.equals( "STX" ) )
		{
			if ( sField.equals( "SNRF") )
			{
				mMapEquality.put( sField, sValue );
			}
			else if ( sField == "UNTO-1" )
			{
				mMapEquality.put( sField, sValue );
			}
		}
		else if ( msCurrentSegment.equals( "MHD" ) )
		{
			if ( sField.equals( "MSRF" ) )
			{
				long nValue = CoreTypes.castToInt( sValue );
				if ( nValue != mnMessageCount )
				{
					sError.append( "Field does not contain the correct messages reference number (field value = " );
					sError.append( sValue);
                    sError.append( ", expected ");
                    sError.append( mnMessageCount );
                    sError.append( " ).");
				}
			}
		}
		else if ( msCurrentSegment.equals( "MTR" ) )
		{
			if ( sField.equals( "NOSG" ) )
			{
				long nValue = CoreTypes.castToInt( sValue );
				if ( nValue != mnSegmentCount )
				{
					sError.append( "Field does not contain the correct number of segments (field value = " );
					sError.append( sValue );
					sError.append( ", counted " );
					sError.append( mnSegmentCount );
					sError.append( " )." );
				}
			}
		}
		else if ( msCurrentSegment.equals( "EOB" ) )
		{
			if ( sField.equals( "NOLI" ) )
			{
				long nValue = CoreTypes.castToInt( sValue );
				if ( nValue != mnGroupCount )
				{
					sError.append( "Field does not contain the correct number of messages (field value = " );
					sError.append( sValue );
                    sError.append( ", counted " );
                    sError.append( mnGroupCount );
                    sError.append( " )." );
				}
			}
		}
		else if ( msCurrentSegment.equals( "RSG" ) )
		{
			if ( sField.equals( "RSGA" ) )
			{
				if ( !mMapEquality.containsKey( "SNRF" ) )
				{
					sError.append("Field does not contain the same value as Interchange/STX/SNRF ('" );
					sError.append( sValue);
					sError.append( "' instead of '')." );
				}
				sExpectedValue = validateEquality( "SNRF", sValue );
				if ( !sExpectedValue.equals("") )
				{
					sError.append( "Field does not contain the same value as Interchange/STX/SNRF ('" );
					sError.append( sValue);
					sError.append( "' instead of '" );
					sError.append( sExpectedValue );
					sError.append( "')." );
				}
			}
			else if ( sField.equals( "RSGB" ) )
			{
				if ( !mMapEquality.containsKey( "UNTO-1" ) )
				{
					sError.append("Field does not contain the same value as Interchange/STX/UNTO/UNTO-1 ('" );
					sError.append( sValue);
					sError.append( "' instead of '')." );
				}
				sExpectedValue = validateEquality( "UNTO-1", sValue );
				if ( !sExpectedValue.equals("") )
				{
					sError.append( "Field does not contain the same value as Interchange/STX/UNTO/UNTO-1 ('" );
					sError.append( sValue);
					sError.append( "' instead of '" );
					sError.append( sExpectedValue );
					sError.append( "')." );
				}
			}
		}
		else if ( msCurrentSegment.equals( "END" ) )
		{
			if ( sField.equals( "NMST" ) )
			{
				long nValue = CoreTypes.castToInt( sValue );
				if ( nValue != mnMessageCount )
				{
					sError.append( "Field does not contain the correct number of messages (field value = " );
					sError.append( sValue);
                    sError.append( ", counted ");
                    sError.append( mnMessageCount );
                    sError.append( " ).");
				}
			}
		}

		return sError.toString();
	}
	
	private String validateHL7( String sField, String sValue )
	{

		return "";
	}

    String validateEquality( String sField, String sValue )
    {
        // if has no field, add it; if map has field compare it and remove it
        if (!mMapEquality.containsKey(sField)) // map has no field?
        {
            mMapEquality.put(sField, sValue);
	        return "";
        }

        // obviously map has the field
        String ret = "";
        if ( !mMapEquality.get(sField).equals(sValue))
            ret = (String)mMapEquality.get(sField);
        mMapEquality.remove(sField);
        return ret;
    }

	public void setCurrentMessageType( String sMessageType )
    {
    	msCurrentMessageType = sMessageType;
    }
}
