package com.cms.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public final class StringUtils {

	public static String escapeSql(String input) {
		String result = input.trim().replace("/", "//").replace("_", "/_").replace("%", "/%");
		return result;
	}

	public static String toLikeAndLowerCaseString(String content) {
		return "%" + StringUtils.escapeSql(content.toLowerCase().trim()) + "%";
	}

	public static String unAccent(String s) {
		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
	}

	public static String removeAccents(String value) {
		if (value == null || value.isEmpty()) {
			return "";
		}
		// Tách các ký tự có dấu thành ký tự gốc và dấu
		String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
		// Pattern để tìm các ký tự dấu
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		// Thay thế ký tự dấu bằng chuỗi rỗng và chuyển sang chữ thường
		return pattern.matcher(temp)
				.replaceAll("")
				.toLowerCase()
				.replaceAll("đ", "d"); // Xử lý riêng chữ 'đ'
	}

}