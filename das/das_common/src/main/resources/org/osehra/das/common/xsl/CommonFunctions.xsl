<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:util="das://org.osehra.das.common.xsl"
  xmlns:datetime_util="das://org.osehra.integration.util.DateUtil"
  xmlns:uuid_util="das://org.osehra.integration.util.UUIDUtil"
  xmlns:base64_util="das://org.osehra.integration.util.Base64Util"
  xmlns:transformer="das://org.osehra.integration.util.xsl.AbstractXSLTransformer"

  exclude-result-prefixes="datetime_util uuid_util base64_util util transformer">

  <!-- Author: David Ellis -->
  <!-- Utility functions that invoke Java methods -->

  <xsl:function name="util:convertCDADateToU62">
    <xsl:param name="CDADate" />
    <xsl:value-of select="datetime_util:convertCDAtoU62($CDADate)" />
  </xsl:function>

  <xsl:function name="util:convertISODateToText">
    <xsl:param name="ISODate" />
    <xsl:param name="Format" />
    <xsl:value-of select="datetime_util:convertISO8601ToText($ISODate, $Format)" />
  </xsl:function>

  <xsl:function name="util:getCurrentDateTime">
    <xsl:value-of select="datetime_util:getCurrentTime()" />
  </xsl:function>

  <xsl:function name="util:getCurrentDateTimeyyyyMMddHHmmss">
    <xsl:value-of select="datetime_util:getCurrentDateTimeyyyyMMddHHmmss()" />
  </xsl:function>

  <xsl:function name="util:generateUUID">
    <xsl:value-of select="uuid_util:generateUUID()" />
  </xsl:function>


  <xsl:function name="util:encodeBase64">
  	<xsl:param name="text" />
  	<xsl:value-of select="base64_util:stringToBase64($text)" />
  </xsl:function>

  <xsl:function name="util:decodeBase64">
  	<xsl:param name="base64" />
  	<xsl:value-of select="base64_util:base64ToString($base64)" />
  </xsl:function>

  <xsl:function name="util:throwTransformerException">
    <xsl:param name="message" />
    <xsl:value-of select="transformer:throwTransformerException($message)" />
  </xsl:function>

</xsl:stylesheet>
