package test1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class test {
	public static void main(String[] args) throws IOException {
		String[] s = new String[10000];
		String a = new String();
		FileReader fr = new FileReader("E:\\a\\1.txt");
		FileWriter fw = new FileWriter("E:\\a\\test.txt");
		BufferedWriter af = new BufferedWriter(fw);
		BufferedReader bf = new BufferedReader(fr);
		int i = 0;
		while ((a = bf.readLine()) != null) {
			// if(a.length()!=0)
			// {while(a.codePointAt(0)==9)
			// a=a.substring(1, a.length());}
			a = a.replaceAll("\t", "");
			a = a.trim();
			a = asc(a);
			a = a.toLowerCase();
			s[i] = a;
			i++;

		}
		bf.close();
		comment(s);
		for (int j = 0; s[j] != null; j++) {
			long c = PJWHash(s[j]);
			s[j] = Long.toString(c);
		}
		constructor(s);
		for (int j = 0; s[j] != null; j++) {
			System.out.println(s[j]);
			af.write(s[j]);
			af.newLine();
		}
		af.close();
	}

	public static String asc(String a) {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < a.length(); i++) {
			if (0 <= a.codePointAt(i) && a.codePointAt(i) <= 127) {
				s.append(a.charAt(i));
			}
		}
		a = s.toString();
		return a;
	}

	public static void constructor(String[] a) {
		String b[] = new String[10000];
		for (int i = 0; a[i + 2] != null; i++) {
			StringBuffer s = new StringBuffer();
			s.append(a[i]);
			s.append(a[i + 1]);
			s.append(a[i + 2]);
			b[i] = s.toString();

		}
		for (int i = 0; a[i] != null; i++)
			a[i] = null;
		a[0] = b[0];
		for (int i = 0; b[i] != null; i++)
			a[i] = b[i];
	}

	public static long PJWHash(String str) {
		long BitsInUnsignedInt = (long) (4 * 8);
		long ThreeQuarters = (long) ((BitsInUnsignedInt * 3) / 4);
		long OneEighth = (long) (BitsInUnsignedInt / 8);
		long HighBits = (long) (0xFFFFFFFF) << (BitsInUnsignedInt - OneEighth);
		long hash = 0;
		long test = 0;
		for (int i = 0; i < str.length(); i++) {
			hash = (hash << OneEighth) + str.charAt(i);
			if ((test = hash & HighBits) != 0)
				hash = ((hash ^ (test >> ThreeQuarters)) & (~HighBits));
		}
		return hash;
	}

	public static void comment(String[] a) {
		String[] b = new String[10000];/* b用于保存处理后的字符串 */
		int j = 0;
		int tag = 0;/*
					 * tag=0代表注释在这一行结束，或者只是一行普通的代码（刚进入循环时候需要对tag进行判断，此时tag=
					 * 0代表注释已经结束，需要重新判断这一行）
					 */
		int p = 0;
		int m = 0;
		for (int i = 0; a[i] != null; i++) {
			if (tag == 0) {
				p = a[i].indexOf("/*");/* 此行有/*开头的注释 */
				if (p != -1) {
					for (m = i + 1; a[m].startsWith("*"); m++)
						;
					if (a[m - 1].endsWith("*/")) {
						tag = 1;/* 第一行有/*，后面找得到注释结束符号，tag=1 */
						if (a[i].endsWith("*/"))
							tag = 0;/* 注释在一行，tag=0 */
					} else
						tag = 2;/* 后面没有注释结束符号，tag=2 */
					if (!a[i + 1].startsWith("*"))
						tag = 0;/* 多行注释只剩第一行，当作单行注释处理 */
					if (!(b[j] = a[i].substring(0, p)).equals(""))
						j++;/* 如果截断之后还有字符，j加一进入下一行 */
					continue;/* 处理完这一行就跳出大循环了，避免tag不等于0遇到后面的if语句会重复处理 */
				}

				if (a[i].startsWith("*")) {
					for (m = i + 1; a[m].startsWith("*"); m++)
						;
					if (a[m - 1].endsWith("*/")) {
						tag = 3;/* 多行注释缺少开始符号，tag=3 */
						if (a[i].endsWith("*/"))
							tag = 0;/* 多行注释只剩最后，当作单行注释处理 */
						continue;/* 处理完这一行就跳出大循环了，避免tag不等于0遇到后面的if语句会重复处理 */
					} else
						tag = 0;/* 这只是一行以*开始的代码，不是注释 */
				}
				b[j] = a[i];
				j++;/* 如果前面判断出不是注释，把这一行存入字符串数组b */
			}
			if (tag == 1) {
				if (a[i].endsWith("*/"))
					tag = 0;/* 说明注释在这一行结束了，后面两个if语句功能相同 */
				continue;
			}
			if (tag == 2) {
				if (!a[i + 1].startsWith("*"))
					tag = 0;
				continue;
			}
			if (tag == 3) {
				if (a[i].endsWith("*/"))
					tag = 0;
				continue;
			}
		}
		for (int i = 0; a[i] != null; i++)
			a[i] = null;
		a[0] = b[0];
		for (int i = 0; b[i] != null; i++)
			a[i] = b[i];

	}
}
