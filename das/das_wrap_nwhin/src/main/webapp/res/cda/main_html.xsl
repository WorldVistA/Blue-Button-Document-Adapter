<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:n3="http://www.w3.org/1999/xhtml"
	xmlns:n1="urn:hl7-org:v3" xmlns:n2="urn:hl7-org:v3/meta/voc" xmlns:voc="urn:hl7-org:v3/voc"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:section="urn:org.osehra.med">

	<!-- Asha Amritraj - Organize Imports for the XSL Processor START -->
	<xsl:import href="custom_cda.xsl" />
	<!-- Asha Amritraj - Organize Imports for the XSL Processor END -->

	<xsl:output method="html" indent="yes" version="4.01"
		encoding="ISO-8859-1" />

	<xsl:template match="/">
		<html>
			<head>
				<xsl:comment>
					Do NOT edit this HTML directly: it was generated via an XSLT
					transformation from a CDA Release 2 XML document.
				</xsl:comment>
				<title>
					<xsl:value-of select="$title" />
				</title>
				<xsl:call-template name="addCSS" />
			</head>
			<body>
				<div id="custom_cda">
					<xsl:apply-templates select="n1:ClinicalDocument" />
				</div>
			</body>
		</html>
	</xsl:template>

	<xsl:template name="addCSS">
		<style type="text/css">
			<xsl:text>
#custom_cda{
  color: #003366;
  background-color: #FFFFFF;
  font-family: Verdana, Tahoma, sans-serif;
  font-size: 10px;
  width:830px;
}

#custom_cda a {
  color: #003366;
  background-color: #FFFFFF;
}

#custom_cda h1 {
  font-size: 11pt;
  font-weight: bold;
}

#custom_cda h2 {
  font-size: 10pt;
  font-weight: bold;
}

#custom_cda h3 {
  font-size: 9pt;
  font-weight: bold;
}

#custom_cda h4 {
  font-size: 8pt;
  font-weight: bold;
}

#custom_cda div {
  width: 100%;
}

#custom_cda table {
  line-height: 10pt;
  width: 100%;
}

#custom_cda tr {
  background-color: #ffffcc;
}

#custom_cda .td_header_label {
  background-color: #3399ff;
}

#custom_cda .tr_header {
  background-color: #CCCCFF;
}


#custom_cda td {
  padding: 0.1cm 0.2cm;
  vertical-align: top;
}

#custom_cda th {
  background-color: #ffd700;
}


#custom_cda .h1center {
  font-size: 11pt;
  font-weight: bold;
  text-align: center;
  width: 100%;
}

#custom_cda .header_table{
  border: 1pt inset #00008b;
}

#custom_cda .narr_table {
  width: 100%;
}

#custom_cda .narr_tr {
  background-color: #ffffcc;
}

#custom_cda .narr_th {
  background-color: #ffd700;
}

#custom_cda .td_label{
  font-weight: bold;
  color: white;
}

#smenu {
    z-index: 1;
    position: absolute;
    top: 45px;
    left: 685px;
	width: 100%;
	float: left;
	text-align: right;
	color: #000;
}
#menu {
	position: absolute;
	top: 45px;
	left: 0px;
    z-index: 1;
	float: left;
	text-align: right;
	color: #000;
	list-style: none;
	line-height: 1;
}
#TipBox {
	display: none;
	position: absolute;
	font-size: 12px;
	font-weight: bold;
	font-family: verdana;
	border: 0px;
	padding: 15px;
	color: black;
	background-color: #FFFFE0;
	width:400px;
}
          </xsl:text>
		</style>
	</xsl:template>


</xsl:stylesheet>