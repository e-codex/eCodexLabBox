<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:pom="http://maven.apache.org/POM/4.0.0">
    <xsl:output omit-xml-declaration="yes" indent="yes"/>
<!--    <xsl:strip-space elements="*"/>-->

<!--    <xsl:template match="node()|@*">-->
<!--        <xsl:copy>-->
<!--            <xsl:apply-templates select="node()|@*"/>-->
<!--        </xsl:copy>-->
<!--    </xsl:template>-->

<!--    <xsl:template match="project/parent"/>-->

<!--    <xsl:template match="processing-instruction('xml-multiple')[contains(., 'parent')]"/>-->

    <xsl:template match="*">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="pom:parent" >

    </xsl:template>

</xsl:stylesheet>