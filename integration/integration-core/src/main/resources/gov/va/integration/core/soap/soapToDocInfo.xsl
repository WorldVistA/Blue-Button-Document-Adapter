<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:s="http://www.w3.org/2003/05/soap-envelope"
	xmlns:b="urn:ihe:iti:xds-b:2007"
	xmlns:scr="http://niem.gov/niem/domains/screening/2.1"
	xmlns:util="das://org.osehra.das.common.xsl"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	exclude-result-prefixes="util">

	<xsl:output method="text" />

	<xsl:include href="/org/osehra/das/common/xsl/CommonFunctions.xsl" />

	<!-- Main -->
	<xsl:template match="/">
	<xsl:for-each select="s:Envelope/s:Body/b:RetrieveDocumentSetRequest/b:DocumentRequest">
		<xsl:value-of select="b:homeCommunityId"/>_<xsl:value-of select="b:RepositoryUniqueId"/>_<xsl:value-of select="b:DocumentUniqueId"/>_
	</xsl:for-each> <!-- Document Request -->
	</xsl:template>

</xsl:stylesheet>
