<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:n3="http://www.w3.org/1999/xhtml"
	xmlns:n1="urn:hl7-org:v3" xmlns:n2="urn:hl7-org:v3/meta/voc" xmlns:voc="urn:hl7-org:v3/voc"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:section="urn:org.osehra.med">

	<!-- Asha Amritraj - Organize Imports for the XSL Processor START -->
	<xsl:import href="/org/osehra/nhin/vap/document/xsl/custom/custom_cda.xsl" />
	<!-- Asha Amritraj - Organize Imports for the XSL Processor END -->

	<xsl:output method="html" indent="yes" version="4.01"
		encoding="ISO-8859-1" />

	<xsl:template match="/">
		<div id="custom_cda">
			<xsl:apply-templates select="n1:ClinicalDocument" />
		</div>
	</xsl:template>
</xsl:stylesheet>