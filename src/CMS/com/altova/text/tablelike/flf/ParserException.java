////////////////////////////////////////////////////////////////////////
//
// ParserException.java
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

package com.altova.text.tablelike.flf;

import com.altova.text.tablelike.MappingException;

public class ParserException extends MappingException {
    int m_Offset = -1;

    public int getOffset() {
        return m_Offset;
    }

    ParserException(String message, int offset) {
        super(message + " at offset #" + offset);
        m_Offset = offset;
    }
}