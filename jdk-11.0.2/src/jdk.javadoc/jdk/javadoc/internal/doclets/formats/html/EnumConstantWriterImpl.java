/*
 * Copyright (c) 2003, 2018, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package jdk.javadoc.internal.doclets.formats.html;


import jdk.javadoc.internal.doclets.formats.html.markup.Table;
import jdk.javadoc.internal.doclets.formats.html.markup.TableHeader;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import jdk.javadoc.internal.doclets.formats.html.markup.HtmlConstants;
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle;
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlTag;
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlTree;
import jdk.javadoc.internal.doclets.formats.html.markup.Navigation;
import jdk.javadoc.internal.doclets.formats.html.markup.StringContent;
import jdk.javadoc.internal.doclets.toolkit.Content;
import jdk.javadoc.internal.doclets.toolkit.EnumConstantWriter;
import jdk.javadoc.internal.doclets.toolkit.MemberSummaryWriter;

/**
 * Writes enum constant documentation in HTML format.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Jamie Ho
 * @author Bhavesh Patel (Modified)
 */
public class EnumConstantWriterImpl extends AbstractMemberWriter
    implements EnumConstantWriter, MemberSummaryWriter {

    public EnumConstantWriterImpl(SubWriterHolderWriter writer, TypeElement typeElement) {
        super(writer, typeElement);
    }

    public EnumConstantWriterImpl(SubWriterHolderWriter writer) {
        super(writer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Content getMemberSummaryHeader(TypeElement typeElement,
            Content memberSummaryTree) {
        memberSummaryTree.addContent(HtmlConstants.START_OF_ENUM_CONSTANT_SUMMARY);
        Content memberTree = writer.getMemberTreeHeader();
        writer.addSummaryHeader(this, typeElement, memberTree);
        return memberTree;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMemberTree(Content memberSummaryTree, Content memberTree) {
        writer.addMemberTree(memberSummaryTree, memberTree);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Content getEnumConstantsDetailsTreeHeader(TypeElement typeElement,
            Content memberDetailsTree) {
        memberDetailsTree.addContent(HtmlConstants.START_OF_ENUM_CONSTANT_DETAILS);
        Content enumConstantsDetailsTree = writer.getMemberTreeHeader();
        enumConstantsDetailsTree.addContent(links.createAnchor(
                SectionName.ENUM_CONSTANT_DETAIL));
        Content heading = HtmlTree.HEADING(HtmlConstants.DETAILS_HEADING,
                contents.enumConstantDetailLabel);
        enumConstantsDetailsTree.addContent(heading);
        return enumConstantsDetailsTree;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Content getEnumConstantsTreeHeader(VariableElement enumConstant,
            Content enumConstantsDetailsTree) {
        enumConstantsDetailsTree.addContent(links.createAnchor(name(enumConstant)));
        Content enumConstantsTree = writer.getMemberTreeHeader();
        Content heading = new HtmlTree(HtmlConstants.MEMBER_HEADING);
        heading.addContent(name(enumConstant));
        enumConstantsTree.addContent(heading);
        return enumConstantsTree;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Content getSignature(VariableElement enumConstant) {
        Content pre = new HtmlTree(HtmlTag.PRE);
        writer.addAnnotationInfo(enumConstant, pre);
        addModifiers(enumConstant, pre);
        Content enumConstantLink = writer.getLink(new LinkInfoImpl(
                configuration, LinkInfoImpl.Kind.MEMBER, enumConstant.asType()));
        pre.addContent(enumConstantLink);
        pre.addContent(" ");
        if (configuration.linksource) {
            Content enumConstantName = new StringContent(name(enumConstant));
            writer.addSrcLink(enumConstant, enumConstantName, pre);
        } else {
            addName(name(enumConstant), pre);
        }
        return pre;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDeprecated(VariableElement enumConstant, Content enumConstantsTree) {
        addDeprecatedInfo(enumConstant, enumConstantsTree);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComments(VariableElement enumConstant, Content enumConstantsTree) {
        addComment(enumConstant, enumConstantsTree);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTags(VariableElement enumConstant, Content enumConstantsTree) {
        writer.addTagsInfo(enumConstant, enumConstantsTree);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Content getEnumConstantsDetails(Content enumConstantsDetailsTree) {
        if (configuration.allowTag(HtmlTag.SECTION)) {
            HtmlTree htmlTree = HtmlTree.SECTION(getMemberTree(enumConstantsDetailsTree));
            return htmlTree;
        }
        return getMemberTree(enumConstantsDetailsTree);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Content getEnumConstants(Content enumConstantsTree,
            boolean isLastContent) {
        return getMemberTree(enumConstantsTree, isLastContent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSummaryLabel(Content memberTree) {
        Content label = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING,
                contents.enumConstantSummary);
        memberTree.addContent(label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableHeader getSummaryTableHeader(Element member) {
        return new TableHeader(contents.enumConstantLabel, contents.descriptionLabel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Table createSummaryTable() {
        String summary = resources.getText("doclet.Member_Table_Summary",
            resources.getText("doclet.Enum_Constant_Summary"),
            resources.getText("doclet.enum_constants"));

        return new Table(configuration.htmlVersion, HtmlStyle.memberSummary)
                .setSummary(summary)
                .setCaption(contents.getContent("doclet.Enum_Constants"))
                .setHeader(getSummaryTableHeader(typeElement))
                .setColumnStyles(HtmlStyle.colFirst, HtmlStyle.colLast)
                .setUseTBody(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSummaryAnchor(TypeElement typeElement, Content memberTree) {
        memberTree.addContent(links.createAnchor(SectionName.ENUM_CONSTANT_SUMMARY));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInheritedSummaryAnchor(TypeElement typeElement, Content inheritedTree) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInheritedSummaryLabel(TypeElement typeElement, Content inheritedTree) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addSummaryLink(LinkInfoImpl.Kind context, TypeElement typeElement, Element member,
            Content tdSummary) {
        Content memberLink = HtmlTree.SPAN(HtmlStyle.memberNameLink,
                writer.getDocLink(context, member, name(member), false));
        Content code = HtmlTree.CODE(memberLink);
        tdSummary.addContent(code);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addInheritedSummaryLink(TypeElement typeElement, Element member, Content linksTree) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addSummaryType(Element member, Content tdSummaryType) {
        //Not applicable.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Content getDeprecatedLink(Element member) {
        String name = utils.getFullyQualifiedName(member) + "." + member.getSimpleName();
        return writer.getDocLink(LinkInfoImpl.Kind.MEMBER, member, name);
    }
}
