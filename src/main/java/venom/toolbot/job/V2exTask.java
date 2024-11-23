package venom.toolbot.job;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import venom.toolbot.enums.TaskStatusEnum;
import venom.toolbot.exception.V2exException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class V2exTask {
    private static final String BASE_URL = "https://www.v2ex.com";
    private static final String BALANCE_URL = BASE_URL + "/balance";
    private static final String MISSION_URL = BASE_URL + "/mission/daily";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36 Edg/131.0.0.0";
    private final Map<String, String> cookies;

    public void run() {
        try {
            startSignIn();
        } catch (V2exException v2exException) {

        } catch (Exception e) {
            log.error("v2ex 签到出现未知异常", e);

        }
        // 不管签到是否成功，都去获取余额信息
        try {
            Pair<Integer, Integer> balance = getBalance();
            if (Objects.isNull(balance.getLeft())) {
                // 今天尚未签到
            } else {
                // 今天签到成功
            }
        } catch (V2exException v2exException) {

        } catch (Exception e) {
            log.error("v2ex 获取余额出现未知异常", e);
        }
    }

    private void startSignIn() throws IOException {
        // 访问每日任务页面
        Document missionDoc = Jsoup.connect(MISSION_URL)
                .cookies(cookies)
                .header("User-Agent", USER_AGENT)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                // .header("Accept-Encoding", "gzip, deflate, br")
                .header("Cache-Control", "no-cache")
                .header("Connection", "keep-alive")
                .header("Upgrade-Insecure-Requests", "1")
                .header("Sec-Fetch-Dest", "document")
                .header("Sec-Fetch-Mode", "navigate")
                .header("Sec-Fetch-Site", "same-origin")
                .header("Sec-Fetch-User", "?1")
                .timeout(10000)
                .maxBodySize(0)
                .ignoreHttpErrors(true)
                .get();
        log.debug("v2ex 签到页面：\n{}", missionDoc);
        // 查找签到按钮
        Element button = missionDoc.selectFirst("input.super.normal.button");
        if (button == null) {
            log.error("无法找到签到按钮，签到页面可能已经发生变化\n{}", missionDoc);
            throw new V2exException(TaskStatusEnum.V2EX_FAILURE_1);
        }

        String onclick = button.attr("onclick");
        String clickEvent = onclick.split("=", 2)[1];
        String url = clickEvent.substring(2, clickEvent.length() - 2);
        log.debug("v2ex 签到按钮：{}", onclick);

        if ("/balance".equals(url)) {
            throw new V2exException(TaskStatusEnum.V2EX_FAILURE_2);
        }

        // 发送签到请求
        Connection.Response response = Jsoup.connect(BASE_URL + url)
                .cookies(cookies)
                .header("Referer", MISSION_URL)
                .data("once", url.split("=")[1])
                .method(Connection.Method.GET)
                .execute();
        log.debug("v2ex 签到响应：{}", response.body().trim());
    }

    private Pair<Integer, Integer> getBalance() throws IOException {
        Document balanceDoc = Jsoup.connect(BALANCE_URL)
                .cookies(cookies)
                .get();
        log.debug("v2ex 余额页面: {}", balanceDoc);

        Elements balanceData = balanceDoc.select("table.data tr:nth-of-type(2)");
        if (balanceData.isEmpty()) {
            log.error("v2ex 无法获取余额列表：{}", balanceDoc);
            throw new V2exException(TaskStatusEnum.V2EX_FAILURE_3);
        }

        String first = balanceData.first().text().trim();
        log.debug("最近签到情况：{}", first);
        String[] balanceInfo = first.split("\\s+");

        LocalDate latestDate = LocalDate.parse(balanceInfo[0]);
        Integer today = Optional.ofNullable(balanceInfo[4]).map(x -> Math.round(Float.parseFloat(x))).orElse(0);
        Integer total = Optional.ofNullable(balanceInfo[5]).map(x -> Math.round(Float.parseFloat(x))).orElse(0);
        if (latestDate.isEqual(LocalDate.now())) {
            log.info("今天签到获得:{} 一共获得:{}", today, total);
            return Pair.of(today, total);
        } else {
            log.info("今天尚未签到，目前一共获取：{}", total);
            return Pair.of(null, total);
        }
    }

    private String coinConversion(Integer copper) {
        int gold = copper / 10000;
        int remainingCopper = copper % 10000;
        int silver = remainingCopper / 100;
        remainingCopper = remainingCopper % 100;
        return gold + "金" + silver + "银" + remainingCopper + "铜";
    }
}
