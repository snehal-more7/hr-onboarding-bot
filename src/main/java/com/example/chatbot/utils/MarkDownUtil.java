package com.example.chatbot.utils;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;

public class MarkDownUtil {

    private static final Parser parser = Parser.builder().build();
    private static final HtmlRenderer renderer = HtmlRenderer.builder().build();

    public static String renderMarkdownToHtml(String markdownText) {
        Node document = parser.parse(markdownText);
        return renderer.render(document);
    }
}
