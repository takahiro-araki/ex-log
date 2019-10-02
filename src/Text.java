
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * テキストファイルを読み込むクラス.
 * 
 * @author takahiro.araki
 *
 */
public class Text {

	public static void main(String[] args) throws IOException, Exception {

		// テキストファイルをリストに入れる
		String path = "C:\\env\\app\\study\\text.txt";
		List<String> text = readString(path);

		Map<LocalDateTime, Integer> logMap = new HashMap<>();

		// 日時とタイムをマップに入れる
		for (int i = 1; i < text.size(); i++) {
			// リストから分割を行い、配列で返す
			String[] arrayText = splitString(text.get(i));
			// マップに詰める
			LocalDateTime date = parseTime(arrayText[0]);
			Integer intTime = Integer.parseInt(arrayText[2]);
			logMap.put(date, intTime);
		}

		Map<LocalDateTime, List<Integer>> createLogMap = createLogMap(logMap);

		List<ResultDomain> resultList = calcLog(createLogMap);

		System.out.println("時刻　　　～500　　　～2000　　　2001～　　　平均応答時間");
		for (ResultDomain a : resultList) {
			System.out.println(a.getTime() + " / " + a.getUnder500() + " / " + a.getUnder2000() + " / "
					+ a.getOver2000() + " / " + a.getAverageTime());
		}

	}

	/**
	 * テキストファイルを読み込みます.
	 * 
	 * @param path 読み込むファイルのパス
	 * @return ファイルの内容
	 * @throws IOException
	 */
	public static List<String> readString(String path) throws IOException {
		return Files.readAllLines(Paths.get(path), Charset.forName("UTF-8"));
	}

	/**
	 * 文字を指定の値で分割し、配列にして返します.
	 * 
	 * @param text 文字データ
	 * @return String型の配列
	 */
	public static String[] splitString(String text) {
		String[] stringElement = text.split("	", 0);
		return stringElement;
	}

	/**
	 * 文字列をdate型にして返します.
	 * 
	 * @param date 日時形式の文字列
	 * @return
	 */
	public static LocalDateTime parseTime(String date) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime ld = LocalDateTime.parse(date, dtf);
		return ld;

	}

	/**
	 * 日時とタイムをマップに詰めます.
	 * 
	 * @param text
	 * @return
	 */
	public static Map<LocalDateTime, Integer> addMap(String text) {
		Map<LocalDateTime, Integer> logMap = null;
		String[] textArray = splitString(text);
		LocalDateTime date = parseTime(textArray[0]);
		Integer intTime = Integer.parseInt(textArray[2]);
		logMap.put(date, intTime);
		return logMap;
	}

	/**
	 * ００時００分から２３時５５分までのリストを返します.
	 * 
	 * @return
	 */
	public static List<LocalDateTime> createDateList() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime startPoint = LocalDateTime.parse("2012/04/07 00:00:00", dtf);
		List<LocalDateTime> timeList = new ArrayList<>();
		timeList.add(startPoint);
		for (int i = 1; i <= 287; i++) {
			startPoint = startPoint.plusMinutes(5);
			timeList.add(startPoint);
		}
		return timeList;
	}

	/**
	 * 時間のリストと結果のMapの紐づけを行います.
	 * 
	 * @param logMap
	 * @return 紐づけの完了したMap
	 */
	public static Map<LocalDateTime, List<Integer>> createLogMap(Map<LocalDateTime, Integer> logMap) {

		// 容れ物となるMapとListを作成
		Map<LocalDateTime, List<Integer>> resultMap = new HashMap<>();
		List<Integer> resultList;
		// 時間リストを用意
		List<LocalDateTime> timeList = createDateList();
		// 結果マップを回し、if文で配分する
		for (int i = 0; i < timeList.size(); i++) {
			resultList = new ArrayList<>();
			for (LocalDateTime ldt : logMap.keySet()) {
				if ((ldt.isAfter(timeList.get(i)) && ldt.isBefore(timeList.get((i + 1)))) || ldt == timeList.get(i)) {
					// 範囲を判定してmapに追加
					resultList.add(logMap.get(ldt));
					resultMap.put(timeList.get(i), resultList);
				}
			}
			// 時刻に該当する応答がひとつもなかった場合、nullでセット
			if (resultMap.get(timeList.get(i)) == null) {
				resultList.add(0);
				resultMap.put(timeList.get(i), resultList);
			}
		}
		return resultMap;
	}

	/**
	 * 各時刻のログ結果を集計し、リストで返します.
	 * 
	 * @param resultMap 結果マップ
	 * @return 結果リスト
	 */
	public static List<ResultDomain> calcLog(Map<LocalDateTime, List<Integer>> resultMap) {
		List<Integer> timeList;
		List<ResultDomain> resultList = new ArrayList<>();
		ResultDomain resultDomain;
		// 時間でソートしてから、リストに詰め替え
		Object[] mapKey = resultMap.keySet().toArray();
		Arrays.sort(mapKey);

		for (Object key : mapKey) {
			timeList = new ArrayList<>();
			timeList = resultMap.get(key);
			resultDomain = new ResultDomain();
			// 時刻をセット
			resultDomain.setTime(key);
			// 各集計結果をセット
			int u500Count = 0;
			int u2000Count = 0;
			int o2000Count = 0;
			int avgTimeCount = 0;
			for (Integer time : timeList) {
				if (time == 0) {
					u500Count = 0;
					u2000Count = 0;
					o2000Count = 0;
					avgTimeCount = 0;
				} else if (time <= 500) {
					u500Count += 1;

				} else if (time <= 2000) {
					u2000Count += 1;
				} else {
					o2000Count += 1;
				}
				avgTimeCount += time;
			}
			avgTimeCount /= timeList.size();

			resultDomain.setUnder500(u500Count);
			resultDomain.setUnder2000(u2000Count);
			resultDomain.setOver2000(o2000Count);
			resultDomain.setAverageTime(avgTimeCount);
			// 各時刻の結果リストをセット
			resultList.add(resultDomain);
		}
		return resultList;
	}
}
