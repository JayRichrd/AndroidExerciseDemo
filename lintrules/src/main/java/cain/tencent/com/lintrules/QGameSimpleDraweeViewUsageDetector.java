package cain.tencent.com.lintrules;

import com.android.ddmlib.Log;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

/**
 * @author cainjiang
 * @date 2018/9/17
 */
public class QGameSimpleDraweeViewUsageDetector extends ResourceXmlDetector {
    private static final String TAG = "QGameSimpleDraweeViewUsageDetector";
    private static final String TAG_NAME = "cain.tencent.com.androidexercisedemo.QGameSimpleDraweeView";

    private static final Class<? extends Detector> DETECTOR_CLASS = QGameSimpleDraweeViewUsageDetector.class;
    private static final EnumSet<Scope> DETECTOR_SCOPE = Scope.RESOURCE_FILE_SCOPE;
    private static final Implementation IMPLEMENTATION = new Implementation(DETECTOR_CLASS, DETECTOR_SCOPE);
    private static final String ISSUE_ID = "QGameSimpleDraweeViewUsage";
    private static final String ISSUE_DESCRIPTION = "Check the QGameSimpleDraweeView's usage.";
    private static final String ISSUE_EXPLANATION = "Check whether set the resize width and resize height for QGameSimpleDraweeView.";
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


    private String reportStrFormat = "QGameSimpleDraweeView应该尽量设置resize的width和height，以降低Bitmap占用内存的空间。具体用法请参考~~。如果QGameSimpleDraweeView达不到你的要求，您也可以直接使用SimpleDraweeView";

    private boolean containResizeWidth = false;
    private boolean containResizeHeight = false;
    private boolean isQGameSimpleDraweeView = false;
    private Attr reportAttr;
    private XmlContext reportXmlContext;

    @Override
    public void beforeCheckProject(Context context) {
        super.beforeCheckProject(context);
    }

    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return ResourceFolderType.LAYOUT == folderType;
    }

    @Override
    public Collection<String> getApplicableElements() {
        return Collections.singletonList(TAG_NAME);
    }

    @Override
    public void visitAttribute(XmlContext context, Attr attribute) {
        super.visitAttribute(context, attribute);
//        String tagName = attribute.getOwnerElement().getTagName();
//        Log.i(TAG, "tag name: " + tagName);
//        Log.i(TAG, "attribute name: " + attribute.getName());
//
//        String prnMain = context.getMainProject().getDir().getPath();
//        String prnCur = context.getProject().getDir().getPath();
//
//        if (tagName.equals(TAG_NAME)) {
//            isQGameSimpleDraweeView = true;
//            if (attribute.getName().equals("qgSdvResizeWidth")) {
//                containResizeWidth = true;
//            } else {
//                containResizeWidth = false;
//                reportAttr = attribute;
//                reportXmlContext = context;
//            }
//
//            if (attribute.getName().equals("qgSdvResizeHeight")) {
//                containResizeHeight = true;
//            } else {
//                containResizeHeight = false;
//                reportAttr = attribute;
//                reportXmlContext = context;
//            }
//        }
    }

    @Override
    public void afterCheckProject(Context context) {
        super.afterCheckProject(context);
//        if ((!containResizeWidth || !containResizeHeight) && isQGameSimpleDraweeView) {
//            reportXmlContext.report(ISSUE,
//                    reportAttr,
//                    reportXmlContext.getLocation(reportAttr),
//                    reportStrFormat);
//        }
    }

    @Override
    public void visitElementAfter(XmlContext context, Element element) {
        super.visitElementAfter(context, element);
        System.out.println("visit element " + element.getTagName() + " finish.");
    }

    @Override
    public void visitElement(XmlContext context, Element element) {
        super.visitElement(context, element);
        System.out.println("visit element " + element.getTagName());
        System.out.println("element name 是否是目标的："+ element.getTagName().equals(TAG_NAME));
        if ((!element.hasAttributeNS("http://schemas.android.com/apk/res-auto","qgSdvResizeHeight") || !element.hasAttributeNS("http://schemas.android.com/apk/res-auto","qgSdvResizeWidth")) && element.getTagName().equals(TAG_NAME)) {
            context.report(ISSUE,
                    element,
                    context.getLocation(element),
                    reportStrFormat);
        }
    }
}
