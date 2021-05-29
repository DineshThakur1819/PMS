package com.pms.pmsmodel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

/**
 * -----------------------------------------------------------------------------
 * CommonUtils : This class is used to convert all the primitive data
 * types in Java to bytes and vice-versa. Could be important while passing
 * information over the network
 * -----------------------------------------------------------------------------
 */

public class CommonUtils
{

	  ////////////////////////HELPERS//////////////////////////////
	        /*
	        public static byte[] longToByteArray(long value)
	        {       
	                byte[] bytes = new byte[8];
	        
	                for(int i = 0, shift = (7) * 8; i < 8; i++, shift -= 8)
	                {   
	                        bytes[i] = (byte)(0xFF & (value >> shift));
	                }
	                
	                //int resultInt = getIntFromByteArray(bytes,length);
	                return bytes;
	        
	        }//end 
	        */
	        
	        public static byte[] intToByteArray(int value)
	        {       
	                byte[] bytes = new byte[4];
	        
	                for(int i = 0, shift = 24; i < 4; i++, shift -= 8)
	                {   
	                        bytes[i] = (byte)(0xFF & (value >> shift));
	                }
	                
	                //int resultInt = getIntFromByteArray(bytes,length);
	                return bytes;
	        
	        }//end 
	        
	        
	        /*
	          public static long toLong(byte[] bytes, long defaultValue)
	          {
	                  long value = defaultValue;
	                  
	                  if(bytes != null)
	                  {
	                          value = 0;
	                          for(int i = 0 ; (i < 8) && (i < bytes.length); i++)
	                          {
	                                  value <<= 8;
	                                  value ^= (long)(bytes[i] & 0xFF);             
	                          }
	                  }
	                  
	                  return value;
	          }//toLong
	          */
	  
	  public static int toInt(byte[] bytes, int defaultValue)
	  {
	          int value = defaultValue;
	          
	          if(bytes != null)
	          {
	                  value = 0;
	                  for(int i = 0 ; (i < 4) && (i < bytes.length); i++)
	                  {
	                          value <<= 8;
	                          value ^= (long)(bytes[i] & 0xFF);             
	                  }
	          }
	          
	          return value;
	  }//toLong
	        
	  
	        
	  public static String byteArrayToString(byte[] data)
	  {
	    StringBuffer buff = new StringBuffer();
	    
	    if(data != null)
	    {
	        for(int i = 0; i < data.length; i++)
	        {
	            byte b = data[i];
	            buff.append((char)b);
	        }
	    }
	    
	    return buff.toString();
	  }//byteArrayToString
	  

	  
	  public static String byteArrayToHexString(byte[] data)
	  {
	    StringBuffer buff = new StringBuffer();
	    
	    if(data != null)
	    {
	        for(int i = 0; i < data.length; i++)
	        {
	            byte b = data[i];
	            int I =  ((char)b) & 0xFF;
	            buff.append(NIBBLE_TO_CHAR[I >>>  4]);
	            buff.append(NIBBLE_TO_CHAR[I & 0x0F]);
	        }
	    }
	    
	    return buff.toString();
	  }//byteArrayToHexString
	  
	  public static String bytetoHexString(byte data)
	  {
		  StringBuffer buff = new StringBuffer();
		  
		  byte b = data;
          int I =  ((char)b) & 0xFF;
          buff.append(NIBBLE_TO_CHAR[I >>>  4]);
          buff.append(NIBBLE_TO_CHAR[I & 0x0F]);
		  
          return buff.toString();
	  }
	  
	  public static byte[] hexStringToByteArray(String hexString)
	  {
	    byte[] result = null;
	    
	    if(hexString != null)
	    {
	        result = new byte[hexString.length()/2];
	        
	        for(int i = 0 ; i < result.length; i++)
	        {
	            int j = i*2;
	            char msb = hexString.charAt(j);
	            char lsb = hexString.charAt(j+1);
	            
	            result[i] = (byte)(char)(((hexCharToInt(msb) << 4) & 0x00F0) | (hexCharToInt(lsb) & 0x000F));
	        }
	    }
	    
	    return result;
	  }//hexStringToByteArray
	  
	  public static boolean isNumber( String s )
	  {
	          if( s == null)return  false;
	          s= s.trim();
	          final char[] digits ={'1','2','3','4','5','6','7','8','9','0'};
	          int NUM_DIGITS=10,l = s.length();
	          for( int i=0; i<l;i++)
	          {
	                  char c=s.charAt(i);
	                  boolean ischar = true;
	                  for(int j=0; j<NUM_DIGITS; j++ )
	                  {
	                          if ( c ==digits[j]) ischar=false;
	                  }
	                  if( ischar) return false;
	          }
	          return true;
	  }

	  static char NIBBLE_TO_CHAR[] = {'0', '1', '2', '3', 
	                                  '4', '5', '6', '7', 
	                                  '8', '9', 'A', 'B', 
	                                  'C', 'D', 'E', 'F'};

	  public static int hexCharToInt(char c)
	  {
	    int i = 0;
	    
	    if((c >= '0') && (c <= '9'))
	        i = c - '0';
	    else if((c >= 'A') && (c <= 'F'))
	        i = c - 'A' + 10;
	    else if((c >= 'a') && (c <= 'f'))
	        i = c - 'a' + 10;
	    
	    return i;
	  }
	  
	  
	  /**
	   * 
	   * @param array1
	   * @param array2
	   * @return 0 = same, 1 = different length, 2 = different data
	   */
	  public static int compareByteArray(byte[] array1, byte[] array2)
	  {
	    int retValue = 0;
	     
	    if((array1 != null) && (array2 != null) &&
	       (array1.length == array2.length))
	    {
	        for(int i = 0; i < array1.length; i++)
	        {
	            if(array1[i] != array2[i])
	            {
	                retValue = 2;
	                break;
	            }
	        }//for
	    }
	    else
	        retValue = 1;
	      
	      return retValue;
	  }
	  
	//  public static byte[] getLLVar(StreamConnectionClient connection) throws IOException
	//  {
//	    byte[] var = null;
//	        byte[] LLBytes = new byte[2];
//	        connection.readBytes(LLBytes);
//	        
	//    
//	        int len = ((((char)LLBytes[0] & 0xFF) - 0x30) * 10) +
//	                                     (((char)LLBytes[1] & 0xFF) - 0x30)             ;
//	        
//	        if(len > 0)
//	        {
//	            var = new byte[len];
//	            connection.readBytes(var);
//	        }
	//    
//	        return var;
	//  }

	  

	  public static byte[] getLLVar(byte[] rawData, int index) 
	  {
	    byte[] var = null;
	        
	    int len = getLL(rawData, index);
	    
	    if(len > 0)
	        {
	            var = new byte[len];
	            System.arraycopy(rawData, index += 2, var, 0, len);
	        }
	    
	        return var;
	  }
	  
	  public static int getLL(byte[] LLBytes, int index) 
	  {
	        int len = ((((char)LLBytes[index++] & 0xFF) - 0x30) * 10) + 
	                   (((char)LLBytes[index++] & 0xFF) - 0x30)           ;
	        
	        return len;
	  }

	   public static byte[] getBytes(short s)
	   {
	        byte[] bytes = new byte[2];

	        bytes[0] = (byte)(0xFF & (s >> 8));
	        bytes[1] = (byte)(0xFF & s);

	        return bytes;
	   }
	  
	  public static short getShort(byte[] byteArr)
	  {
	    short value = (short)(((byteArr[0] & 0xFF) << 8)  +
	                           (byteArr[1] & 0xFF));
	    return value;
	  }
	  
	  public String generateASCIILength(String data, int len)
	  {
	    String asciiLen = null;
	    int dataLen = 0;
	    if(data != null)
	    {
	      dataLen = data.length();
	    }
	    
	      asciiLen = String.valueOf(dataLen);
	    
	    while(asciiLen.length() < len)
	    {
	      asciiLen = "0" + asciiLen;
	    }
	    
	    return asciiLen;
	  }
	  
	    public static String getLLString(String data)
	    {
	      return getLLString(data.length());
	    }
	  
	    public static String getLLString(int len)
	    {
	      StringBuffer buff = new StringBuffer();

	      if(len < 9)
	        buff.append('0');
	        
	      buff.append(len);
	      
	      return buff.toString();
	    }
	  
	    public static String getLLString(int len, int LLlen)
	    {
	      StringBuffer buff = new StringBuffer();

	      buff.append(len);
	      
	      while(buff.length() < LLlen)
	        buff.insert(0, '0');
	      
	      return buff.toString();
	    }

	    public static int LOG_DEBUG     = 5;
	    public static int LOG_INFO      = 4;
	    public static int LOG_WARNING   = 3;
	    public static int LOG_ERROR     = 2;
	    public static int LOG_FATAL     = 1;
	    
	    //private static int LOG_OFF      = 0;

	    public static final String[] HEADER = { "", 
	                                            "FATAL => ",
	                                            "ERROR => ",
	                                            "WARNING => ",
	                                            "INFO => ",
	                                            "DEBUG => "};
	    
	    private static int LOG_LEVEL = LOG_DEBUG + 1;
	    public static void log(int level, String message) 
	    {
	      //check the bounds....
	      if((LOG_LEVEL < level) || (LOG_LEVEL < LOG_FATAL) || (level < LOG_FATAL))
	      {
	          return;
	      }
	      else if(level > LOG_DEBUG)
	        level = LOG_DEBUG;  
	      //END check the bounds....

	      
	      StringBuffer buff = new StringBuffer();
	      buff.append('[');
	      buff.append(Thread.currentThread().getName());
	      buff.append(']');
	      buff.append(HEADER[level]);
	      buff.append(message);
	      
	      System.out.println(buff.toString());
	  }

	    public static void writeLV(OutputStream out, byte[] data) throws IOException
	    {
	      int dataLen = 0;
	      if(data != null)
	        dataLen = data.length;
	      out.write(dataLen);

	      if((data != null) && (dataLen > 0))
	        out.write(data);
	    }
	    
	    public static void writeImage( OutputStream out, byte[] data )throws IOException
	    {
	        int dataLen =0;
	        if( data !=null )
	        {
	                dataLen = data.length;
	        }
	        
	        writeLV( out, String.valueOf( dataLen ));
	        if( dataLen > 0 )
	        {
	                out.write(data, 0, dataLen);
	        }
	    }
	    public static byte[] readImage( InputStream in ) throws IOException
	    {
	        byte[] data = null;
	        String len = readLVString(in);
	        int l = Integer.parseInt(len);
	        if( l > 0 )
	        {
	                data = new byte[l];
	                in.read(data, 0, l);
	        }
	        return data;
	        
	    }
	    public static void writeLV(OutputStream out, String data) throws IOException
	    {
	      int dataLen = 0;
	      if(data != null)
	        dataLen = data.length();
	      out.write(dataLen);

	      if((data != null) && (dataLen > 0))
	        out.write(data.getBytes());
	    }
	    
	    public static byte[] readLV(InputStream in) throws IOException
	    {
	      byte[] data = null;
	      int dataLen = in.read();
	      if(dataLen > 0)
	      {
	        data = new byte[dataLen];
	        in.read(data);
	      }
	      
	      return data;
	    }
	    	    
	    public static byte[] readLV( InputStream in, int len ) throws IOException
	    {
	        byte[] data = new byte[len];
	        in.read(data, 0, len);
	        return data;
	        
	    }
	    public static String readLVString(InputStream in) throws IOException
	    {
	        String value = null;
	        byte[] rawBytes = readLV(in);
	        if(rawBytes != null)
	                value = new String(rawBytes);
	        
	        return value;
	    }
	    
	    static public String formatPaymentCard( String cardNumber )
	    {
	        if( cardNumber.length() >=16)
	        {
	            StringBuffer buf = new StringBuffer();
	            int len = cardNumber.length();
	            String first = cardNumber.substring(0,4);
	            String last = cardNumber.substring(len-4, len);
	            buf.append( first);
	            buf.append("-xxxx-xxxx-");
	            buf.append( last );
	            return buf.toString();                
	                
	        }
	        else 
	        { 
	        	return cardNumber; 
	        }
	    }
	    
	    public static final short setShort(byte[] buffer, short bOff, short value)
	    {
	        buffer[bOff++] = (byte)(0xFF & (value >> 8));
	        buffer[bOff++] = (byte)(0xFF & value );

	        return bOff;
	    }
	    
	    
	    public static byte[] shortToByteArray(short value)
	    {
	        byte[] bytes = new byte[2];

	        bytes[0] = (byte)(0xFF & (value >> 8));
	        bytes[1] = (byte)(0xFF & value );

	        //int resultInt = getIntFromByteArray(bytes,length);
	        return bytes;
	    }//shortToByteArray

	    public static String GetCSW(int SW) {
			if(SW == 0)
				return "OK";
			else if(SW == 0x01)
				return "Error";
			else if(SW == 0x02)
				return "Timeout";
			else
				return "ERROR";
		}
		
		public static int HexToInt(String strHex) throws Exception {
			if( (strHex.length() < 1) || (strHex.length() > 2))
			  throw new Exception("HEX Lenth error.");
			
			strHex = strHex.toUpperCase();

			int ret = 0;
			byte []bData = strHex.getBytes();
			int len = bData.length;
			
			for(int i = 0; i < len; i++)
			{
				ret = (ret << (i * 4)) + HexValue(bData[i]);
			}
			
			return ret;
		}
		
		public static int HexValue(int ch_hex) throws Exception
		{
			if( (ch_hex >= (int)'0') && (ch_hex <= (int)'9') )
			{
				return ((int)ch_hex - 0x030);
			}
			else if((ch_hex >= (int)'A') && (ch_hex <= (int)'F'))
			{
				return ((int)ch_hex - (int)'A') + 0x0A;
			}
			else
			{
				throw new Exception("HEX Chart error.");
			}
		}
		
	public static String hexToString(String hex) {

		StringBuilder sb = new StringBuilder();

		// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
		for (int i = 0; i < hex.length() - 1; i += 2) {

			// grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			// convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			// convert the decimal to character
			sb.append((char) decimal);
		}

		return sb.toString();
	}

//    public static String computeTax(float amount) {
//    	float t = amount * DeviceSettingsFragment.TAX_PERCENT; 
//    	// t+"".replaceAll("\\.0*$", "")
//    	return round(t, 2)+"";
//    }
    
    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);       
        return bd;
    }
    
    public static boolean validateCardNumber(String cardNumber) {
        int sum = 0;
        int digit = 0;
        int addend = 0;
        boolean doubled = false;
        for (int i = cardNumber.length () - 1; i >= 0; i--) {
            digit = Integer.parseInt (cardNumber.substring (i, i + 1));
            if (doubled) {
                addend = digit * 2;
                if (addend > 9) {
                    addend -= 9; 
                }
            } else {
                addend = digit;
            }
            sum += addend;
            doubled = !doubled;
        }
        return (sum % 10) == 0;
    }

	// Function to perform left padding
	public static String
	leftPadding(String input, char ch, int L)
	{

		String result
				= String

				// First left pad the string
				// with space up to length L
				.format("%" + L + "s", input)

				// Then replace all the spaces
				// with the given character ch
				.replace(' ', ch);

		// Return the resultant string
		return result;
	}

	// Function to perform right padding
	public static String
	rightPadding(String input, char ch, int L)
	{

		String result
				= String

				// First right pad the string
				// with space up to length L
				.format("%" + (-L) + "s", input)

				// Then replace all the spaces
				// with the given character ch
				.replace(' ', ch);

		// Return the resultant string
		return result;
	}
}
