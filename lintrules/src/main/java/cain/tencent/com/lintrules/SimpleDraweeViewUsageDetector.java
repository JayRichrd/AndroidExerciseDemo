package cain.tencent.com.lintrules;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;

import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

/**
 * @author cainjiang
 * @date 2018/10/29
 */
public class SimpleDraweeViewUsageDetector extends ResourceXmlDetector {
    private static final String TAG = "SimpleDraweeViewUsageDetector";
    private static final String TAG_NAME = "com.facebook.drawee.view.SimpleDraweeView";

    private static final Class<? extends Detector> DETECTOR_CLASS = SimpleDraweeViewUsageDetector.class;
    private static final EnumSet<Scope> DETECTOR_SCOPE = Scope.RESOURCE_FILE_SCOPE;
    private static final Implementation IMPLEMENTATION = new Implementation(DETECTOR_CLASS, DETECTOR_SCOPE);
    private static final String ISSUE_ID = "SimpleDraweeViewUsage";
    private static final String ISSUE_DESCRIPTION = "Check the SimpleDraweeView's usage.";
    private static final String ISSUE_EXPLANATION = "Check whether use SimpleDraweeView.";
    private static final Category ISSUE_CATEGORY = Category.MESSAGES;
    private static final int ISSUE_PRIORITY = 9;
    private static final Severity ISSUE_SEVERITY = Severity.ERROR;

    public static final Issue ISSUE = Issue.create(
            ISSUE_ID,
            ISSUE_DESCRIPTION,
            ISSUE_EXPLANATION,
            ISSUE_CATEGORY,
            ISSUE_PRIORITY,
            ISSUE_SEVERITY,
            IMPLEMENTATION
    );

    private String reportStrFormat = "应该尽量少用SimpleDraweeView";

    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return ResourceFolderType.LAYOUT == folderType;
    }

    @Override
    public Collection<String> getApplicableElements() {
        return Collections.singletonList(TAG_NAME);
    }

    @Override
    public void visitElement(XmlContext context, Element element) {
        super.visitElement(context, element);
        System.out.println("visit element " + element.getTagName());
        System.out.println("element name 是否是目标的：" + element.getTagName().equals(TAG_NAME));
        if (element.getTagName().equals(TAG_NAME)) {
            context.report(ISSUE,
                    element,
                    context.getLocation(element),
                    reportStrFormat);
        }
    }

}
