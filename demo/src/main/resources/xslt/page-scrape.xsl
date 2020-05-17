<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:output method="xml"/>

  <xsl:param name="scheme"/>
  <xsl:param name="authority"/>
  <xsl:param name="correlationId"/>
  <xsl:param name="scrapedAt"/>

  <xsl:template match="/html">
    <xsl:element name="standings">
      <xsl:apply-templates select="//div[@data-standingtype='Complete']//tr[position() &gt; 1]"/>
      <xsl:element name="metadata">
        <xsl:element name="lastUpdateAt">
          <xsl:value-of select="normalize-space(substring-after(//div[@data-standingtype='Complete']/following-sibling::div/p[1], ':'))"/>
        </xsl:element>
        <xsl:element name="correlationId">
          <xsl:value-of select="$correlationId"/>
        </xsl:element>
        <xsl:element name="scrapedAt">
          <xsl:value-of select="$scrapedAt"/>
        </xsl:element>
      </xsl:element>
    </xsl:element>
  </xsl:template>

  <xsl:template match="tr">
    <xsl:element name="entry">
      <xsl:attribute name="position">
        <xsl:value-of select="normalize-space(td[1])"/>
      </xsl:attribute>
      <xsl:element name="team">
        <xsl:element name="name">
          <xsl:element name="full">
            <xsl:value-of select="normalize-space(td[4]/a/span[@class='kick__table--show-desktop'])"/>
          </xsl:element>
          <xsl:element name="short">
            <xsl:value-of select="normalize-space(td[4]/a/span[@class='kick__table--show-mobile'])"/>
          </xsl:element>
        </xsl:element>
        <xsl:element name="link">
          <xsl:attribute name="rel">logo</xsl:attribute>
          <xsl:attribute name="href">
            <xsl:value-of select="td[3]/a/img/@src"/>
          </xsl:attribute>
          <xsl:attribute name="type">image</xsl:attribute>
        </xsl:element>
        <xsl:element name="link">
          <xsl:attribute name="rel">about</xsl:attribute>
          <xsl:attribute name="href">
            <xsl:value-of select="concat($scheme, '://', $authority, td[3]/a/@href)"/>
          </xsl:attribute>
          <xsl:attribute name="type">text/html</xsl:attribute>
        </xsl:element>
      </xsl:element>
      <xsl:element name="statistics">
        <xsl:element name="points">
          <xsl:value-of select="normalize-space(td[11])"/>
        </xsl:element>
        <xsl:element name="games">
          <xsl:attribute name="played">
            <xsl:value-of select="normalize-space(td[5])"/>
          </xsl:attribute>
          <xsl:element name="wins">
            <xsl:value-of select="normalize-space(td[6]/span[@class='kick__table--show-desktop'])"/>
          </xsl:element>
          <xsl:element name="draws">
            <xsl:value-of select="normalize-space(td[7])"/>
          </xsl:element>
          <xsl:element name="losses">
            <xsl:value-of select="normalize-space(td[8])"/>
          </xsl:element>
        </xsl:element>
        <xsl:element name="goals">
          <xsl:element name="for">
            <xsl:value-of select="normalize-space(substring-before(td[9], ':'))"/>
          </xsl:element>
          <xsl:element name="against">
            <xsl:value-of select="normalize-space(substring-after(td[9], ':'))"/>
          </xsl:element>
        </xsl:element>
      </xsl:element>
    </xsl:element>
  </xsl:template>

</xsl:stylesheet>
