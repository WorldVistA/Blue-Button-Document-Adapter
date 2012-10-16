<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:util="das://org.osehra.das.common.xsl.util"
  xmlns:datetime_util="xalan://org.osehra.das.common.date.DateUtil"
  xmlns:uuid_util="xalan://org.osehra.das.common.uuid.UUIDUtil"
  xmlns:hl7DateTime_util="xalan://org.osehra.das.common.date.hl7.HL7DateUtil"
  exclude-result-prefixes="uuid_util datetime_util util "
>
  <xsl:function name="util:generateUUID">
    <xsl:value-of select="uuid_util:generateMessageId()" />
  </xsl:function>
  <xsl:function name="util:generateCompleteUUID">
    <xsl:value-of select="uuid_util:generateUUID()" />
  </xsl:function>
  <xsl:function name="util:getCurrentTime">
    <xsl:value-of select="datetime_util:getCurrentTime()" />
  </xsl:function>

  <xsl:function name="util:getDateFromHL7DateTSString">
    <xsl:param name="dateString" />
    <xsl:value-of select="hl7DateTime_util:dateFromString($dateString)" />
  </xsl:function>

  <xsl:function name="util:formatDateyyyyMMddHHmmssSSSZ">
   	<xsl:param name="date" />
    <xsl:value-of select="datetime_util:formatDateyyyyMMddHHmmssSSSZ($date)" />
  </xsl:function>

  <xsl:function name="util:getCurrentDateTimeyyyyMMddHHmmss">
    <xsl:value-of select="datetime_util:getCurrentDateTimeyyyyMMddHHmmss()" />
  </xsl:function>
  <xsl:function name="util:getDateFromString">
    <xsl:param name="dateString" />
    <xsl:value-of select="datetime_util:dateFromString($dateString)" />
  </xsl:function>
</xsl:stylesheet>
