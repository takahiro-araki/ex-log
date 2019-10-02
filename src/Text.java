
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
 * �e�L�X�g�t�@�C����ǂݍ��ރN���X.
 * 
 * @author takahiro.araki
 *
 */
public class Text {

	public static void main(String[] args) throws IOException, Exception {

		// �e�L�X�g�t�@�C�������X�g�ɓ����
		String path = "C:\\env\\app\\study\\text.txt";
		List<String> text = readString(path);

		Map<LocalDateTime, Integer> logMap = new HashMap<>();

		// �����ƃ^�C�����}�b�v�ɓ����
		for (int i = 1; i < text.size(); i++) {
			// ���X�g���番�����s���A�z��ŕԂ�
			String[] arrayText = splitString(text.get(i));
			// �}�b�v�ɋl�߂�
			LocalDateTime date = parseTime(arrayText[0]);
			Integer intTime = Integer.parseInt(arrayText[2]);
			logMap.put(date, intTime);
		}

		Map<LocalDateTime, List<Integer>> createLogMap = createLogMap(logMap);

		List<ResultDomain> resultList = calcLog(createLogMap);

		System.out.println("�����@�@�@�`500�@�@�@�`2000�@�@�@2001�`�@�@�@���ω�������");
		for (ResultDomain a : resultList) {
			System.out.println(a.getTime() + " / " + a.getUnder500() + " / " + a.getUnder2000() + " / "
					+ a.getOver2000() + " / " + a.getAverageTime());
		}

	}

	/**
	 * �e�L�X�g�t�@�C����ǂݍ��݂܂�.
	 * 
	 * @param path �ǂݍ��ރt�@�C���̃p�X
	 * @return �t�@�C���̓��e
	 * @throws IOException
	 */
	public static List<String> readString(String path) throws IOException {
		return Files.readAllLines(Paths.get(path), Charset.forName("UTF-8"));
	}

	/**
	 * �������w��̒l�ŕ������A�z��ɂ��ĕԂ��܂�.
	 * 
	 * @param text �����f�[�^
	 * @return String�^�̔z��
	 */
	public static String[] splitString(String text) {
		String[] stringElement = text.split("	", 0);
		return stringElement;
	}

	/**
	 * �������date�^�ɂ��ĕԂ��܂�.
	 * 
	 * @param date �����`���̕�����
	 * @return
	 */
	public static LocalDateTime parseTime(String date) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime ld = LocalDateTime.parse(date, dtf);
		return ld;

	}

	/**
	 * �����ƃ^�C�����}�b�v�ɋl�߂܂�.
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
	 * �O�O���O�O������Q�R���T�T���܂ł̃��X�g��Ԃ��܂�.
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
	 * ���Ԃ̃��X�g�ƌ��ʂ�Map�̕R�Â����s���܂�.
	 * 
	 * @param logMap
	 * @return �R�Â��̊�������Map
	 */
	public static Map<LocalDateTime, List<Integer>> createLogMap(Map<LocalDateTime, Integer> logMap) {

		// �e�ꕨ�ƂȂ�Map��List���쐬
		Map<LocalDateTime, List<Integer>> resultMap = new HashMap<>();
		List<Integer> resultList;
		// ���ԃ��X�g��p��
		List<LocalDateTime> timeList = createDateList();
		// ���ʃ}�b�v���񂵁Aif���Ŕz������
		for (int i = 0; i < timeList.size(); i++) {
			resultList = new ArrayList<>();
			for (LocalDateTime ldt : logMap.keySet()) {
				if ((ldt.isAfter(timeList.get(i)) && ldt.isBefore(timeList.get((i + 1)))) || ldt == timeList.get(i)) {
					// �͈͂𔻒肵��map�ɒǉ�
					resultList.add(logMap.get(ldt));
					resultMap.put(timeList.get(i), resultList);
				}
			}
			// �����ɊY�����鉞�����ЂƂ��Ȃ������ꍇ�Anull�ŃZ�b�g
			if (resultMap.get(timeList.get(i)) == null) {
				resultList.add(0);
				resultMap.put(timeList.get(i), resultList);
			}
		}
		return resultMap;
	}

	/**
	 * �e�����̃��O���ʂ��W�v���A���X�g�ŕԂ��܂�.
	 * 
	 * @param resultMap ���ʃ}�b�v
	 * @return ���ʃ��X�g
	 */
	public static List<ResultDomain> calcLog(Map<LocalDateTime, List<Integer>> resultMap) {
		List<Integer> timeList;
		List<ResultDomain> resultList = new ArrayList<>();
		ResultDomain resultDomain;
		// ���ԂŃ\�[�g���Ă���A���X�g�ɋl�ߑւ�
		Object[] mapKey = resultMap.keySet().toArray();
		Arrays.sort(mapKey);

		for (Object key : mapKey) {
			timeList = new ArrayList<>();
			timeList = resultMap.get(key);
			resultDomain = new ResultDomain();
			// �������Z�b�g
			resultDomain.setTime(key);
			// �e�W�v���ʂ��Z�b�g
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
			// �e�����̌��ʃ��X�g���Z�b�g
			resultList.add(resultDomain);
		}
		return resultList;
	}
}
