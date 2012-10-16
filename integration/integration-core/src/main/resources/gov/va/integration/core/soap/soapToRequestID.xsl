<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:s="http://www.w3.org/2003/05/soap-envelope"
	xmlns:a="http://www.w3.org/2005/08/addressing"
	xmlns:scr="http://niem.gov/niem/domains/screening/2.1"
	xmlns:util="das://org.osehra.das.common.xsl"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	exclude-result-prefixes="util">

	<xsl:output method="text" />

	<xsl:include href="/org/osehra/das/common/xsl/CommonFunctions.xsl" />

	<!-- Main -->
	<xsl:template match="/">
	<xsl:for-each select="s:Envelope/s:Header">
		<xsl:value-of select="a:MessageID"/>
	</xsl:for-each> <!-- header -->
	</xsl:template>

</xsl:stylesheet>
