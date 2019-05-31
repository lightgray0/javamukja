package spring.utility.javamukja;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class Utility {
//	public static int rcount(int bbsno,ReplyInter rinter){
//        return rinter.rcount(bbsno);
//   }
	
	public static String rpaging(int total, int nowPage, int recordPerPage, String col, String word, String url,
			int nPage, int bbsno) {
		int pagePerBlock = 5; // 블럭당 페이지 수
		int totalPage = (int) (Math.ceil((double) total / recordPerPage)); // 전체 페이지
		int totalGrp = (int) (Math.ceil((double) totalPage / pagePerBlock));// 전체 그룹
		int nowGrp = (int) (Math.ceil((double) nPage / pagePerBlock)); // 현재 그룹
		int startPage = ((nowGrp - 1) * pagePerBlock) + 1; // 특정 그룹의 페이지 목록 시작
		int endPage = (nowGrp * pagePerBlock); // 특정 그룹의 페이지 목록 종료

		StringBuffer str = new StringBuffer();
		str.append("<div style='text-align:center'>");
		str.append("<ul class='pagination'> ");
		int _nowPage = (nowGrp - 1) * pagePerBlock; // 10개 이전 페이지로 이동

		if (nowGrp >= 2) {
			str.append("<li><a href='" + url + "?col=" + col + "&word=" + word + "&nowPage=" + nowPage + "&bbsno="
					+ bbsno + "&nPage=" + _nowPage + "'>이전</A></li>");
		}

		for (int i = startPage; i <= endPage; i++) {
			if (i > totalPage) {
				break;
			}

			if (nPage == i) {
				str.append("<li class='active'><a href=#>" + i + "</a></li>");
			} else {
				str.append("<li><a href='" + url + "?col=" + col + "&word=" + word + "&nowPage=" + nowPage + "&bbsno="
						+ bbsno + "&nPage=" + i + "'>" + i + "</A></li>");
			}
		}

		_nowPage = (nowGrp * pagePerBlock) + 1; // 10개 다음 페이지로 이동
		if (nowGrp < totalGrp) {
			str.append("<li><A href='" + url + "?col=" + col + "&word=" + word + "&nowPage=" + nowPage + "&bbsno="
					+ bbsno + "&nPage=" + _nowPage + "'>다음</A></li>");
		}
		str.append("</ul>");
		str.append("</div>");

		return str.toString();

	}

	// UploadSave 파일 대신해서 존재하는 메소드 (multipart 객체를 통해)
	// sendFile()대신 saveFileSpring()로 복사
	public static String saveFileSpring(MultipartFile multipartFile, String basePath) {
		// input form's parameter name
		String fileName = "";
		// original file name
		String originalFileName = multipartFile.getOriginalFilename();
		// file content type
		String contentType = multipartFile.getContentType();
		// file size
		long fileSize = multipartFile.getSize();

		System.out.println("fileSize: " + fileSize);
		System.out.println("originalFileName: " + originalFileName);

		InputStream inputStream = null;
		OutputStream outputStream = null;

		try {
			if (fileSize > 0) { // 파일이 존재한다면
				// 인풋 스트림을 얻는다.
				inputStream = multipartFile.getInputStream();

				File oldfile = new File(basePath, originalFileName);

				if (oldfile.exists()) {
					for (int k = 0; true; k++) {
						// 파일명 중복을 피하기 위한 일련 번호를 생성하여
						// 파일명으로 조합
						oldfile = new File(basePath, "(" + k + ")" + originalFileName);

						// 조합된 파일명이 존재하지 않는다면, 일련번호가
						// 붙은 파일명 다시 생성
						if (!oldfile.exists()) { // 존재하지 않는 경우
							fileName = "(" + k + ")" + originalFileName;
							break;
						}
					}
				} else {
					fileName = originalFileName;
				}
				// make server full path to save
				String serverFullPath = basePath + "\\" + fileName;

				System.out.println("fileName: " + fileName);
				System.out.println("serverFullPath: " + serverFullPath);

				outputStream = new FileOutputStream(serverFullPath);

				// 버퍼를 만든다.
				int readBytes = 0;
				byte[] buffer = new byte[8192];

				while ((readBytes = inputStream.read(buffer, 0, 8192)) != -1) {
					outputStream.write(buffer, 0, readBytes);
				}
				outputStream.close();
				inputStream.close();

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return fileName;
	}

	// 직업 코드에 매칭되는 해당 문자열 value를 반환하는 메소드
	public static String getCodeValue(String code) {
		String value = null;
		Hashtable codes = new Hashtable();
		codes.put("A01", "회사원");
		codes.put("A02", "전산관련직");
		codes.put("A03", "연구전문직");
		codes.put("A04", "각종학교학생");
		codes.put("A05", "일반자영업");
		codes.put("A06", "공무원");
		codes.put("A07", "의료인");
		codes.put("A08", "법조인");
		codes.put("A09", "종교/언론/예술인");
		codes.put("A10", "기타");

		value = (String) codes.get(code);

		return value;
	}

	// getDay()를 통해 받아온 날짜(List 타입)를 가지고서
	// 날짜 비교( 현재 날짜 혹은 1일전, 2일전까지의 날짜 게시물은 new이미지를 붙인다)
	public static boolean compareDay(String wdate) {
		boolean flag = false;
		List<String> list = getDay();

		if (wdate.equals(list.get(0)) // 오늘
				|| wdate.equals(list.get(1)) // 1일전
				|| wdate.equals(list.get(2))) { // 2일전
			flag = true;
		}

		return flag;
	}

	// 현재 날짜를 받아서 String으로 고치고 List에 담아 반환하는 메소드
	private static List<String> getDay() {
		List<String> list = new ArrayList<String>();
		// "yyyy-MM-dd"에서 소문자 mm으로 쓰면 안됨
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < 3; i++) {
			list.add(sd.format(cal.getTime())); // 현재날짜 넣고
			cal.add(Calendar.DATE, -1); // 그전날짜로 바꾸고 다시 올라가서 그전 날짜 넣고...
										// Calendar.DATE는 상수로서 5, 하루 단위로 -1한다는 의미

		}

		return list;
	}

	public static String checkNull(String str) {
		if (str == null)
			str = "";
		return str;
	}

	/**
	 * @param totalRecord   전체 레코드수
	 * @param nowPage       현재 페이지
	 * @param recordPerPage 페이지당 레코드 수
	 * @param col           검색 컬럼
	 * @param word          검색어
	 * @return 페이징 생성 문자열
	 */
	public static String paging(int totalRecord, int nowPage, int recordPerPage, String col, String word, String url) {
		int pagePerBlock = 5; // 블럭당 페이지 수
		int totalPage = (int) (Math.ceil((double) totalRecord / recordPerPage)); // 전체 페이지
		int totalGrp = (int) (Math.ceil((double) totalPage / pagePerBlock));// 전체 그룹
		int nowGrp = (int) (Math.ceil((double) nowPage / pagePerBlock)); // 현재 그룹
		int startPage = ((nowGrp - 1) * pagePerBlock) + 1; // 특정 그룹의 페이지 목록 시작
		int endPage = (nowGrp * pagePerBlock); // 특정 그룹의 페이지 목록 종료

		StringBuffer str = new StringBuffer();
		str.append("<div style='text-align:center'>");
		str.append("<ul class='pagination'> ");
		int _nowPage = (nowGrp - 1) * pagePerBlock; // 이전 그룹의 마지막 페이지 값
		if (nowGrp >= 2) {
			str.append(
					"<li><a href='" + url + "?col=" + col + "&word=" + word + "&nowPage=" + _nowPage + "'>이전</A></li>");
		}

		for (int i = startPage; i <= endPage; i++) {
			if (i > totalPage) {
				break;
			}

			if (nowPage == i) {
				// 현재 페이지에서 현재 페이지를 누르면 링크에 #만 더 들어갈 뿐이다, #은 빼도 무관
				// 현재 페이지가 아닌 다른 페이지를 누르면 파라미터를 통해 다시 list.jsp를 호출
				str.append("<li class='active'><a href=>" + i + "</a></li>");
			} else {
				str.append("<li><a href='" + url + "?col=" + col + "&word=" + word + "&nowPage=" + i + "'>" + i
						+ "</A></li>");
			}
		}

		_nowPage = (nowGrp * pagePerBlock) + 1; // 이후 그룹의 첫번째 페이지 값
		if (nowGrp < totalGrp) {
			str.append(
					"<li><A href='" + url + "?col=" + col + "&word=" + word + "&nowPage=" + _nowPage + "'>다음</A></li>");
		}
		str.append("</ul>");
		str.append("</div>");

		return str.toString();
	}

	public static void deleteFile(String upDir, String oldfile) {
		File file = new File(upDir, oldfile);
		if (file.exists())
			file.delete();
	}
}