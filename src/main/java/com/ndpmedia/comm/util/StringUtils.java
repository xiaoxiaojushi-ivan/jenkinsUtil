package com.ndpmedia.comm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class StringUtils {
	private static final char[] symbols = new char[36];
	private static final char[] digitSymbols = new char[10];
	private static final Random random = new Random(System.currentTimeMillis());

	public String copyright() {
		return "Copyright NDP Media Corp. 2015";
	}

	public static Integer toInt(String value) {
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
		}
		return null;
	}

	public static Long toLong(String value) {
		try {
			return Long.valueOf(value);
		} catch (Exception e) {
		}
		return null;
	}

	public static Float toFloat(String value) {
		try {
			return Float.valueOf(value);
		} catch (Exception e) {
		}
		return null;
	}

	public static Double toDouble(String value) {
		try {
			return Double.valueOf(value);
		} catch (Exception e) {
		}
		return null;
	}

	public static Boolean toBoolean(String value) {
		try {
			return Boolean.valueOf(value);
		} catch (Exception e) {
		}
		return null;
	}

	public static int toInt(String value, int dflt) {
		try {
			return Integer.valueOf(value).intValue();
		} catch (Exception e) {
		}
		return dflt;
	}

	public static long toLong(String value, long dflt) {
		try {
			return Long.valueOf(value).longValue();
		} catch (Exception e) {
		}
		return dflt;
	}

	public static float toFloat(String value, float dflt) {
		try {
			return Float.valueOf(value).floatValue();
		} catch (Exception e) {
		}
		return dflt;
	}

	public static double toDouble(String value, double dflt) {
		try {
			return Double.valueOf(value).doubleValue();
		} catch (Exception e) {
		}
		return dflt;
	}

	public static boolean toBoolean(String value, boolean dflt) {
		try {
			return Boolean.valueOf(value).booleanValue();
		} catch (Exception e) {
		}
		return dflt;
	}

	/**
	 * @param str
	 * @param delimiter
	 * @return The string array splitted by delimiter
	 */
	public static String[] split(String str, String delimiter) {
		if (delimiter.length() == 1) {
			return split(str, delimiter.charAt(0));
		}

		List<String> strList = new ArrayList<String>();

		int index = 0;
		int start = 0;
		while ((index = str.indexOf(delimiter, start)) != -1) {
			if (start == index) {
				strList.add(null);
			} else {
				strList.add(str.substring(start, index));
			}

			start = index + 1;
		}
		strList.add(str.substring(start));

		return strList.toArray(new String[0]);
	}

	/**
	 * @param str
	 * @param delimiter
	 * @return The string array splitted by delimiter
	 */
	public static String[] split(String str, final char delimiter) {
		List<String> strList = new ArrayList<String>();

		if (delimiter == '\1' || delimiter == '\2' || delimiter == '\3') {
			int index = 0;
			int start = 0;
			while ((index = str.indexOf(delimiter, start)) != -1) {
				if (start == index) {
					strList.add(null);
				} else {
					strList.add(str.substring(start, index));
				}

				start = index + 1;
			}
			strList.add(str.substring(start));
		} else {
			int len = str.length();
			char prevChar = 0;
			StringBuilder builder = new StringBuilder();
			boolean quote = false;
			for (int i = 0; i < len; i++) {
				char c = str.charAt(i);

				switch (c) {
				case '\"':
					if (quote) {
						if (c == prevChar) {
							if (builder.length() > 0) {
								builder.append(c);
							} else {
								quote = false;
							}
						} else {
							if (i < len - 1) {
								char nextChar = str.charAt(i + 1);
								if (c != nextChar) {
									quote = false;
								}
							} else {
								quote = false;
							}
						}
					} else {
						quote = !quote;
					}
					break;
				default:
					if (c == delimiter) {
						if (quote) {
							builder.append(c);
						} else {
							strList.add(builder.toString());
							builder = new StringBuilder();
						}
					} else {
						builder.append(c);
					}
				}

				if (prevChar == c && prevChar == '\"') {
					prevChar = 0;
				} else {
					prevChar = c;
				}
			}
			if (builder.length() >= 0) {
				strList.add(builder.toString());
			}
		}

		return strList.toArray(new String[0]);
	}

	public static String[] toStringArray(String value) {
		int len = value.length();
		boolean quote = false;
		List<String> fields = new ArrayList<String>();
		StringBuilder token = new StringBuilder();

		for (int i = 0; i < len; i++) {
			char c = value.charAt(i);
			switch (c) {
			case '\t':
			case ' ':
				if (!quote)
					continue;
				token.append(c);
				break;
			case '"':
			case '\'':
				quote = !quote;
				break;
			case ',':
			case '/':
			case ':':
			case ';':
			case '\\':
			case '|':
				if (!quote) {
					fields.add(token.toString());
					token = new StringBuilder();
				} else {
					token.append(c);
				}
				break;
			default:
				token.append(c);
			}
		}
		if (token.length() > 0) {
			fields.add(token.toString());
		}
		return (String[]) fields.toArray(new String[0]);
	}

	public static String stringsToCommaSeparated(Collection<String> values) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (String string : values) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(string);
			i++;
		}
		return sb.toString();
	}

	public static String randomString(int length) {
		Random rnd = new Random();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(symbols[rnd.nextInt(symbols.length)]);
		}
		return sb.toString();
	}

	public static String randomNumber(int length) {
		Random rnd = new Random();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(digitSymbols[rnd.nextInt(digitSymbols.length)]);
		}
		return sb.toString();
	}

	public static String unquote(String value) {
		if (value == null) {
			return null;
		}
		int start = 0;
		int end = value.length();
		if (start == end) {
			return value;
		}

		if (value.charAt(0) == '"') {
			start = 1;
		}
		if ((start == 1) && (value.charAt(value.length() - 1) == '"')) {
			end = value.length() - 1;
			return value.substring(start, end);
		}
		return value;
	}

	public static String singleQuote(String value) {
		return quote(value, '\'');
	}

	public static String quote(String value) {
		return quote(value, '"');
	}

	public static String quote(String value, char c) {
		if (value == null) {
			return value;
		}

		StringBuilder builder = new StringBuilder();

		if (value.charAt(0) != c) {
			builder.append(c);
		}
		builder.append(value);

		if (value.charAt(value.length() - 1) != c) {
			builder.append(c);
		}

		return builder.toString();
	}

	public static String[] arrayConcat(String[] arrayOne, String[] arrayTwo) {
		String[] retVal = new String[arrayOne.length + arrayTwo.length];
		System.arraycopy(arrayOne, 0, retVal, 0, arrayOne.length);
		System.arraycopy(arrayTwo, 0, retVal, arrayOne.length, arrayTwo.length);
		return retVal;
	}

	public static String removeFirstChars(String seq, Function<Character, Boolean> f) {
		StringBuilder sb = new StringBuilder();

		int len = seq.length();
		boolean remove = true;
		for (int i = 0; i < len; i++) {
			char ch = seq.charAt(i);
			if (remove) {
				remove = ((Boolean) f.apply(Character.valueOf(ch)))
						.booleanValue();
			}
			if (!remove) {
				sb.append(ch);
			}
		}

		return sb.toString();
	}

	public static String escapeChars(String value) {
		StringBuilder sb = new StringBuilder();
		int length = value.length();
		for (int i = 0; i < length; i++) {
			sb.append(escapeChar(Character.valueOf(value.charAt(i))));
		}
		return sb.toString();
	}

	public static String escapeChar(Character c) {
		StringBuilder sb = new StringBuilder();
		switch (c.charValue()) {
		case '"':
			sb.append("\\\"");
			break;
		case '\'':
			sb.append("\\'");
			break;
		case '\\':
			sb.append("\\\\");
			break;
		default:
			sb.append(c);
		}
		return sb.toString();
	}

	public static String generateGUID() {
		StringBuilder sb = new StringBuilder();

		long rnd = random.nextLong();
		long tmp = System.currentTimeMillis();

		sb.append(Long.toHexString(tmp)).append("-")
				.append(Long.toHexString(rnd));
		return sb.toString();
	}

	public static String ltrim(String value) {
		if (value == null) {
			return null;
		}
		int index = 0;
		while ((index < value.length()) && (value.charAt(index) <= ' ')) {
			index++;
		}
		return value.substring(index);
	}

	public static String rtrim(String value) {
		if (value == null) {
			return null;
		}
		int index = value.length() - 1;
		while ((index > 0) && (value.charAt(index) <= ' ')) {
			index--;
		}
		return value.substring(0, index + 1);
	}

	public static boolean isEmpty(String value) {
		return (value == null) || (value.isEmpty());
	}

	public static String capitalize(String value) {
		if (isEmpty(value)) {
			return value;
		}
		StringBuilder stringBuilder = new StringBuilder(value);
		stringBuilder.setCharAt(0,
				Character.toUpperCase(stringBuilder.charAt(0)));
		return stringBuilder.toString();
	}

	public static String concat(String separator, String[] parts) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (String s : parts) {
			if (i > 0) {
				sb.append(separator);
			}
			sb.append(s);
			i++;
		}
		return sb.toString();
	}

	public static String concatAndWrap(String first, String separator,
			String last, String[] parts) {
		StringBuilder sb = new StringBuilder();
		sb.append(first);
		int i = 0;
		for (String s : parts) {
			if (i > 0) {
				sb.append(separator);
			}
			sb.append(s);
			i++;
		}
		sb.append(last);
		return sb.toString();
	}

	public static String removeLastPart(String text, String separator) {
		StringBuilder sb = new StringBuilder();
		String[] split = text.split(separator);

		if (split.length == 0) {
			return null;
		}

		for (int i = 0; i < split.length - 1; i++) {
			if (i > 0) {
				sb.append(separator);
			}
			sb.append(split[i]);
		}
		return sb.toString();
	}

	public static String removePart(String text, String separator, int pos) {
		StringBuilder sb = new StringBuilder();
		String[] split = text.split(separator);
		if ((pos < 0) || (pos >= split.length)) {
			return null;
		}
		int i = 0;
		for (int k = 0; i < split.length; i++) {
			if (i != pos) {
				if (k > 0) {
					sb.append(separator);
				}
				sb.append(split[i]);
				k++;
			}
		}
		return sb.toString();
	}

	public static boolean containsCtrlChar(String text) {
		int ix = 0;
		for (int length = text.length(); ix < length; ix++) {
			char c = text.charAt(ix);
			if ((c < ' ') && (c != '\t') && (c != '\n') && (c != '\r')) {
				return true;
			}
		}
		return false;
	}

	public static String removeCtrlChars(String text) {
		return text.replaceAll("[\\p{Cntrl}&&[^\\r\\n\\t\\x7F]]", "");
	}

	static {
		for (int idx = 0; idx < 10; idx++) {
			symbols[idx] = (char) (48 + idx);
			digitSymbols[idx] = (char) (48 + idx);
		}
		for (int idx = 10; idx < 36; idx++)
			symbols[idx] = (char) (97 + idx - 10);
	}
	/**
	 * @param sdate
	 * @return
	 * 以友好的方式显示时间
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		if(time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();
		
		//判断是否是同一天
		String curDate = dateFormater.get().format(cal.getTime());
		String paramDate = dateFormater.get().format(time);
		if(curDate.equals(paramDate)){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if(hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"分钟前";
			else 
				ftime = hour+"小时前";
			return ftime;
		}
		
		long lt = time.getTime()/86400000;
		long ct = cal.getTimeInMillis()/86400000;
		int days = (int)(ct - lt);		
		if(days == 0){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if(hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"分钟前";
			else 
				ftime = hour+"小时前";
		}
		else if(days == 1){
			ftime = "昨天";
		}
		else if(days == 2){
			ftime = "前天";
		}
		else if(days > 2 && days <= 10){ 
			ftime = days+"天前";			
		}
		else if(days > 10){			
			ftime = dateFormater.get().format(time);
		}
		return ftime;
	}
	
	
	public static String friendly_time(Date time) {
		if(time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();
		
		//判断是否是同一天
		String curDate = dateFormater.get().format(cal.getTime());
		String paramDate = dateFormater.get().format(time);
		if(curDate.equals(paramDate)){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if(hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"分钟前";
			else 
				ftime = hour+"小时前";
			return ftime;
		}
		
		long lt = time.getTime()/86400000;
		long ct = cal.getTimeInMillis()/86400000;
		int days = (int)(ct - lt);		
		if(days == 0){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if(hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"分钟前";
			else 
				ftime = hour+"小时前";
		}
		else if(days == 1){
			ftime = "昨天";
		}
		else if(days == 2){
			ftime = "前天";
		}
		else if(days > 2 && days <= 10){ 
			ftime = days+"天前";			
		}
		else if(days > 10){			
			ftime = dateFormater.get().format(time);
		}
		return ftime;
	}
	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	private final static ThreadLocal<SimpleDateFormat> timeFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};
	/**
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return timeFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String toJSON(Object object){
		SerializerFeature feature = SerializerFeature.DisableCircularReferenceDetect;
		String jsonString = JSON.toJSONString(object,feature);
		return jsonString;
	}
}