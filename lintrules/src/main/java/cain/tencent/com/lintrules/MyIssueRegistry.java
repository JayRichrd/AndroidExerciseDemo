package cain.tencent.com.lintrules;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

import java.util.Arrays;
import java.util.List;

/**
 * @author cainjiang
 * @date 2018/9/14
 */
public class MyIssueRegistry extends IssueRegistry {
    @Override
    public List<Issue> getIssues() {
        System.out.println("MyIssueRegistry lint rules works.");
        return Arrays.asList(LoggerUsageDetector.ISSUE, QGameSimpleDraweeViewUsageDetector.ISSUE, SimpleDraweeViewUsageDetector.ISSUE);
    }
}
