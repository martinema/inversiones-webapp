package otros;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PoolDataExtractor {

	public static List<PoolData> extractPoolData(String pageText) {
		List<PoolData> poolDataList = new ArrayList<>();

		// Regular expression to match pool data
		Pattern pattern = Pattern.compile(
				"([A-Za-z0-9\\-]+)[\\s\\$]+(\\d+\\.\\d+)[\\s\\$]+(\\d+,?\\d+\\.\\d+)[\\s\\$]+(\\d+\\.\\d+)[\\s\\$]+(\\d+,?\\d+)[\\s\\$]+(\\d+)[\\s\\$]+(\\d+\\.\\d+%)");

		Matcher matcher = pattern.matcher(pageText);

		while (matcher.find()) {
			String pool = matcher.group(1);
			Double totalFees = Double.parseDouble(matcher.group(2).replace(",", ""));
			Double totalIncentives = Double.parseDouble(matcher.group(3).replace(",", ""));
			Double totalVotes = Double.parseDouble(matcher.group(4).replace(",", ""));

			PoolData poolData = new PoolData(pool, totalFees, totalIncentives, totalVotes);
			poolDataList.add(poolData);
		}

		return poolDataList;
	}

	public static void main(String[] args) throws IOException {
		String url = "https://dapp.gt3.finance/vote/voting"; // Replace with the actual URL
//		String pageText = getPageText(url);
		String pageText = "<page>\n"
				+ "Vote Deposit  your voting power  into the pools of the projects you want to support. Select the pools you want to incentivize, distribute your voting power and proceed to vote, thus beginning to receive passive income. Current epoch details Total distributed $ 290,789.2938 Today's distribution:   $ 2,611.75 You are not connected Connect your wallet to interact with the dApp and vote in the pools you choose.\n"
				+ "CNKT+-USDT $ 41,454.12 9,073,264.65 12,281.68 $ 6,435.26 2,000,000 0 134.67%\n"
				+ "Fees  +  Incentives 0%\n" + "ZHT-USDT $ 37.42 13,789.89 17.36 $ 0.00 0 0 4.29%\n"
				+ "Fees  +  Incentives 0%\n" + "GT3-USDT $ 12,946.26 607,588.44 155.41 $ 1,052.61 49,999.99 0 67.96%\n"
				+ "Fees  +  Incentives 0%\n" + "GT3-FIT $ 997.52 46,797.07 129.56 $ 1,135.43 20,000 7,500 47.1%\n"
				+ "Fees  +  Incentives 0%\n" + "FIT-USDT $ 319.23 1,356.77 190.34 $ 1,095.39 11,500 0 66.95%\n"
				+ "Fees  +  Incentives 0%\n" + "GT3-WBTC $ 6,074.72 288,554.27 $ 1,052.61 49,999.99 58.74%\n"
				+ "Fees  +  Incentives 0%\n" + "GT3-RNT $ 1,597.96 75,021.98 102.98 $ 2,188.94 49,999.99 6,300 40.65%\n"
				+ "Fees  +  Incentives 0%\n" + "GT3-ZHT $ 209.70 9,893.08 980.98 $ 0.00 0 0 19.54%\n"
				+ "Fees  +  Incentives 0%\n" + "WBTC-RNT $ 192.48 1,067.18 $ 2,705.53 15,000 52.88%\n"
				+ "Fees  +  Incentives 0%\n" + "</page>";

		List<PoolData> poolDataList = extractPoolData(pageText);

		for (PoolData poolData : poolDataList) {
			System.out.println("Pool: " + poolData.getPool() + " [1]");
			System.out.println("Total Fees: " + poolData.getTotalFees() + " [2]");
			System.out.println("Total Incentives: " + poolData.getTotalIncentives() + " [3]");
			System.out.println("Total Votes: " + poolData.getTotalVotes() + " [4]");
			System.out.println("---");
		}
	}

	public static String getPageText(String urlString) throws IOException {
		URL url = new URL(urlString);
		java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
		connection.connect();
		java.io.InputStreamReader reader = new java.io.InputStreamReader(connection.getInputStream());
		StringBuilder pageText = new StringBuilder();
		int c = reader.read();
		while (c != -1) {
			pageText.append((char) c);
			c = reader.read();
		}
		reader.close();
		connection.disconnect();
		return pageText.toString();
	}

}
