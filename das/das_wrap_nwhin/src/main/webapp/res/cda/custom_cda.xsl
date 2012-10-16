<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:n3="http://www.w3.org/1999/xhtml"
	xmlns:n1="urn:hl7-org:v3" xmlns:n2="urn:hl7-org:v3/meta/voc" xmlns:voc="urn:hl7-org:v3/voc"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:section="urn:org.osehra.med">

	<xsl:output method="html" indent="yes" version="4.01"
		encoding="ISO-8859-1" />

	<xsl:variable name="snomedCode">2.16.840.1.113883.6.96</xsl:variable>
	<xsl:variable name="snomedProblemCode">55607006</xsl:variable>
	<xsl:variable name="snomedProblemCode2">404684003</xsl:variable>
	<xsl:variable name="snomedProblemCode3">418799008</xsl:variable>
	<xsl:variable name="snomedAllergyCode">416098002</xsl:variable>
	
	<xsl:variable name="loincCode">2.16.840.1.113883.6.1</xsl:variable>
	<xsl:variable name="loincProblemCode">11450-4</xsl:variable>
	<xsl:variable name="loincAllergyCode">48765-2</xsl:variable>
	<xsl:variable name="loincMedCode">10160-0</xsl:variable>
	<xsl:variable name="loincEncounterCode">46240-8</xsl:variable>
	<xsl:variable name="loincResultsCode">30954-2</xsl:variable>
	<xsl:variable name="loincProceduresCode">47519-4</xsl:variable>
	<xsl:variable name="loincImmunizationsCode">11369-6</xsl:variable>
	<xsl:variable name="loincVitalsCode">8716-3</xsl:variable>
	
	<xsl:variable name="vitalsTemplateCode">2.16.840.1.113883.10.20.1.32</xsl:variable>
	<xsl:variable name="labsTemplateCode">2.16.840.1.113883.10.20.1.32</xsl:variable>
	<xsl:variable name="immunizationsTemplateCode">2.16.840.1.113883.10.20.1.32</xsl:variable>
	<xsl:variable name="allergyTemplateCode">2.16.840.1.113883.10.20.1.18</xsl:variable>
	<xsl:variable name="problemTemplateCode">2.16.840.1.113883.10.20.1.28</xsl:variable>

	<xsl:variable name="row">"ClinicalDocument/"</xsl:variable>

	<xsl:variable name="isKaiser"
		select="boolean(/n1:ClinicalDocument/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:id[@root='1.3.6.1.4.1.26580'])" />


	<!-- global variable title -->
	<xsl:variable name="title">
		<xsl:choose>
			<xsl:when test="string-length(/n1:ClinicalDocument/n1:title)  &gt;= 1">
				<xsl:value-of select="/n1:ClinicalDocument/n1:title" />
			</xsl:when>
			<xsl:when test="/n1:ClinicalDocument/n1:code/@displayName">
				<xsl:value-of select="/n1:ClinicalDocument/n1:code/@displayName" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>Clinical Document</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

	<!-- CDA document -->
	<xsl:template match="n1:ClinicalDocument">
		<h1 class="h1center">
			<xsl:value-of select="$title" />
		</h1>
		<!-- START display top portion of clinical document -->
		<b>
			<xsl:text>Created On: </xsl:text>
		</b>
		<xsl:call-template name="getCreatedOnDate" />
		<xsl:call-template name="dateRange" />
		<br/>
		<br/>
		<xsl:call-template name="recordTarget" />
		<xsl:call-template name="documentGeneral" />
		<xsl:call-template name="documentationOf" />
		<xsl:call-template name="author" />
		<xsl:call-template name="componentof" />
		<xsl:call-template name="participant" />
		<xsl:call-template name="dataEnterer" />
		<xsl:call-template name="authenticator" />
		<xsl:call-template name="informant" />
		<xsl:call-template name="informationRecipient" />
		<xsl:call-template name="legalAuthenticator" />
		<xsl:call-template name="custodian" />
		<!-- END display top portion of clinical document -->
		<hr align="left" color="teal" size="2" width="100%" />
		<!-- Structured Content -->
		<xsl:choose>
			<xsl:when test="not(//n1:nonXMLBody)">
				<xsl:if
					test="count(/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component[n1:section]) &gt; 1">
					<xsl:call-template name="make-toc" />
					<xsl:apply-templates select="n1:component/n1:structuredBody" />
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<!-- produce human readable document content -->
				<xsl:apply-templates select="n1:component/n1:nonXMLBody" />
				<xsl:apply-templates
					select="n1:component/n1:structuredBody/n1:component/n1:section/n1:entry/n1:act/n1:entryRelationship" />
			</xsl:otherwise>
		</xsl:choose>
		<br />
		<br />
		<!-- Bottom portion of display -->
		<xsl:call-template name="emergencyContact" />
		<xsl:call-template name="bottomline" />
		<xsl:call-template name="footer" />
	</xsl:template>

	<xsl:template name="dateRange">
		<xsl:if
			test="/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:performer/n1:effectiveTime/n1:low/@value">
			<xsl:text disable-output-escaping="yes">&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;</xsl:text>
			<b>
				<xsl:text>Date Range: </xsl:text>
			</b>
			<xsl:choose>
				<xsl:when
					test="string-length(/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:performer/n1:effectiveTime/n1:low/@value)=0">
					<xsl:call-template name="na" />
				</xsl:when>
				<xsl:when
					test="starts-with(/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:performer/n1:effectiveTime/n1:low/@value,' ')">
					<xsl:call-template name="na" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="formatDateFull">
						<xsl:with-param name="date"
							select="/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:performer/n1:effectiveTime/n1:low/@value" />
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
			<b>
				<xsl:text disable-output-escaping="yes"> - </xsl:text>
			</b>
			<xsl:choose>
				<xsl:when
					test="string-length(/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:performer/n1:effectiveTime/n1:high/@value)=0">
					<xsl:call-template name="na" />
				</xsl:when>
				<xsl:when
					test="starts-with(/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:performer/n1:effectiveTime/n1:high/@value,' ')">
					<xsl:call-template name="na" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="formatDateFull">
						<xsl:with-param name="date"
							select="/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:performer/n1:effectiveTime/n1:high/@value" />
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<!-- generate table of contents -->
	<xsl:template name="make-toc">
		<xsl:if test="n1:component/n1:structuredBody">
			<div>
				<h3>
					<a name="toc">Table of Contents</a>
				</h3>
				<ul>
					<xsl:for-each
						select="n1:component/n1:structuredBody/n1:component/n1:section/n1:title">
						<xsl:if
							test="../n1:code/@code=$loincAllergyCode
							or ../n1:code/@code=$loincMedCode
							or ../n1:code/@code=$loincProblemCode
							or ../n1:code/@code=$loincEncounterCode
							or ../n1:code/@code=$loincResultsCode
							or ../n1:code/@code=$loincProceduresCode
							or ../n1:code/@code=$loincImmunizationsCode
							or ../n1:code/@code=$loincVitalsCode">
							<li>
								<a href="#{generate-id(.)}">
									<xsl:value-of select="." />
								</a>
							</li>
						</xsl:if>
					</xsl:for-each>
				</ul>
			</div>
		</xsl:if>
	</xsl:template>

	<xsl:template name="emergencyContact">
		<br/><br/>
		<xsl:choose>
			<xsl:when
				test="string-length(/n1:ClinicalDocument/n1:participant[@typeCode='IND']/n1:associatedEntity[@classCode='NOK'])>0">
				<table>
					<tr>
						<td width="100px" valign="top" align='left'>
							<b>Emergency Contact: </b>
						</td>
						<td valign="top">
							<xsl:call-template name="getParticipant">
								<xsl:with-param name="participant"
									select="/n1:ClinicalDocument/n1:participant[@typeCode='IND']/n1:associatedEntity[@classCode='NOK']" />
							</xsl:call-template>
						</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:otherwise>
				<b></b>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="footer">
		<div id="TipBox">
			<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
		</div>
		<!-- ASHA AMRITRAJ - Commented body tag since this will be included in 
			anothre HTML <body> -->
		<script type="text/javascript">
			var TipBoxID = "TipBox";
			var tip_box_id;
			function
			findPosX(obj)
			{
			var curleft = 0;
			if(obj.offsetParent)
			while(1)
			{
			curleft +=
			obj.offsetLeft;
			if(!obj.offsetParent)
			break;
			obj = obj.offsetParent;
			}
			else if(obj.x)
			curleft += obj.x;
			return curleft;
			}

			function findPosY(obj)
			{
			var curtop = 0;
			if(obj.offsetParent)
			while(1)
			{
			curtop +=
			obj.offsetTop;
			if(!obj.offsetParent)
			break;
			obj = obj.offsetParent;
			}
			else if(obj.y)
			curtop += obj.y;
			return curtop;
			}

			function HideTip() {
			tip_box_id.style.display = "none";
			}

			function ShowTip() {
			tip_box_id.style.display = "block";
			}

			function ToggleTip() {
			if
			(tip_box_id.style.display == "none"){
			tip_box_id.style.display =
			"block";
			}
			else if (tip_box_id.style.display
			== "block"){
			tip_box_id.style.display = "none";
			}
			}

			function DisplayTip(me,offX,offY)
			{
			var content = me.innerHTML;
			var tdLength =
			me.parentNode.offsetWidth;
			var textLength = me.innerHTML.length;
			if(((textLength-1)*10) >
			tdLength) {
			var tipO = me;
			tip_box_id =
			document.getElementById(TipBoxID);
			var x = findPosX(me);
			var y =
			findPosY(me);
			var left = x + offX - 100;

			if( left &lt; 0) {
			left = 0;
			}
			var top = y + offY - 10;

			tip_box_id.style.left = String(parseInt(left)
			+ 'px');
			tip_box_id.style.width = String('400px');
			
			tip_box_id.style.top = String(parseInt(top) + 'px');
			tip_box_id.innerHTML = content;
			tip_box_id.style.display = "block";
			tipO.onmouseout = HideTip;
			}
			}

			function DisplayText(me,offX,offY) {
			var
			content = me.innerHTML;
			var tdLength =
			me.parentNode.offsetWidth;
			var
			textLength = me.innerHTML.length;
			if(((textLength-1)*10) > tdLength) {
			var tipO = me;
			tip_box_id =
			document.getElementById(TipBoxID);
			var x =
			findPosX(me);
			var y =
			findPosY(me);
			var left = x + offX - 300;

			if( left
			&lt; 0) {
			left = 0;
			}
			var top = y + offY + 75;

			tip_box_id.style.left =
			String(parseInt(left) + 'px');
			tip_box_id.style.top =
			String(parseInt(top) + 'px');
			tip_box_id.style.width = String('400px');
			tip_box_id.innerHTML = content;
			tip_box_id.style.display = "none";
			tipO.onclick = ToggleTip;
			}
			}
	
	      </script>
	</xsl:template>

	<xsl:template name="getParticipant">
		<xsl:param name="participant" />
		<p>
			<xsl:call-template name="getName">
				<xsl:with-param name="name"
					select="$participant/n1:associatedPerson/n1:name" />
			</xsl:call-template>
			<xsl:if test="$participant/n1:addr">
				<xsl:choose>
					<xsl:when test="$isKaiser">
						<xsl:call-template name="getSingleAddress">
							<xsl:with-param name="addr" select="$participant/n1:addr" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="getAddress">
							<xsl:with-param name="addr" select="$participant/n1:addr" />
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
			<xsl:choose>
				<xsl:when test="$participant/n1:telecom/@value">
					<xsl:for-each select="$participant/n1:telecom">
						<xsl:call-template name="getTelecom">
							<xsl:with-param name="telecom" select="." />
						</xsl:call-template>
					</xsl:for-each>
				</xsl:when>
				<xsl:otherwise>
					<br />
					<b>
						<xsl:text>Tel: CONTACT PHONE MISSING</xsl:text>
					</b>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:if test="$participant/n1:code/n1:originalText">
				<br />
				<b>Relationship:</b>
				<xsl:value-of select="$participant/n1:code/n1:originalText" />
			</xsl:if>
		</p>
	</xsl:template>

	<xsl:template name="getSingleAddress">
		<xsl:param name="addr" />
		<xsl:if test="$addr/n1:streetAddressLine != ' '">
			<br />
			<xsl:if test="string-length($addr/n1:streetAddressLine)>0">
				<xsl:value-of select="$addr/n1:streetAddressLine" />
			</xsl:if>

			<br />
			<xsl:value-of select="$addr/n1:city" />
			,
			<xsl:value-of select="$addr/n1:state" />
			,
			<xsl:value-of select="$addr/n1:postalCode" />
		</xsl:if>
	</xsl:template>

	<xsl:template name="getAddress">
		<xsl:param name="addr" />
		<xsl:if test="$addr/n1:streetAddressLine != ' '">
			<xsl:for-each select="$addr/n1:streetAddressLine">
				<br />
				<xsl:if test="string-length($addr/n1:streetAddressLine)>0">
					<xsl:value-of select="." />
				</xsl:if>
			</xsl:for-each>
			<br />
			<xsl:value-of select="$addr/n1:city" />
			,
			<xsl:value-of select="$addr/n1:state" />
			,
			<xsl:value-of select="$addr/n1:postalCode" />
		</xsl:if>
	</xsl:template>

	<xsl:template name="getTelecom">
		<xsl:param name="telecom" />
		<br />
		<xsl:if test="string-length($telecom/@value)>0">
			<xsl:value-of select="$telecom/@value" />
			<xsl:choose>
				<xsl:when test="./@use='HP' ">
					<b>
						<xsl:text> Home</xsl:text>
					</b>
				</xsl:when>
				<xsl:when test="./@use='WP' ">
					<b>
						<xsl:text> Work</xsl:text>
					</b>
				</xsl:when>
				<xsl:when test="./@use='HV' ">
					<b>
						<xsl:text> Vacation</xsl:text>
					</b>
				</xsl:when>
				<xsl:when test="./@use='MC' ">
					<b>
						<xsl:text> Mobile</xsl:text>
					</b>
				</xsl:when>
				<xsl:otherwise>
					<b>
						<xsl:text></xsl:text>
					</b>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<!-- Get a Name -->
	<xsl:template name="getName">
		<xsl:param name="name" />
		<xsl:choose>
			<xsl:when test="string-length($name/n1:family)=0">
			</xsl:when>
			<xsl:when test="$name/n1:family">
				<xsl:for-each select="$name/n1:given">
					<xsl:text> </xsl:text>
					<xsl:value-of select="." />
				</xsl:for-each>
				<xsl:text> </xsl:text>
				<xsl:if test="string-length($name/n1:family)>0">
					<xsl:value-of select="$name/n1:family" />
				</xsl:if>
				<xsl:text> </xsl:text>
				<xsl:if test="string-length($name/n1:suffix)>0">
					<xsl:if test="$name/n1:suffix != ' '">
						<xsl:text>, </xsl:text>
						<xsl:value-of select="$name/n1:suffix" />
					</xsl:if>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$name" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Get Author -->
	<xsl:template name="getAuthor">
		<xsl:variable name="author" />
		<xsl:call-template name="getName">
			<xsl:with-param name="name"
				select="n1:author/n1:assignedAuthor/n1:assignedPerson/n1:name" />
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="$author">
				<xsl:value-of
					select="n1:author/n1:assignedAuthor/n1:assignedPerson/n1:name" />
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<!-- StructuredBody -->

	<!-- Component/Section -->
	<xsl:template match="n1:component/n1:section" name="detailSection">
		<xsl:choose>
			<xsl:when test="n1:code[@code=$loincProblemCode]"> <!-- took out "and count(n1:text/n1:table/n1:thead/n1:tr/n1:th)!=3" -->
				<xsl:apply-templates select="n1:title" />
				<xsl:call-template name="probComments">
					<xsl:with-param select="." name="section" />
				</xsl:call-template>
				<xsl:call-template name="problemDetails">
					<xsl:with-param select="." name="section" />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="n1:code[@code=$loincAllergyCode]">
				<xsl:apply-templates select="n1:title" />
				<xsl:call-template name="allergyComments">
					<xsl:with-param select="." name="section" />
				</xsl:call-template>
				<xsl:call-template name="allergyDetails">
					<xsl:with-param select="." name="section" />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="n1:code[@code=$loincMedCode]">
				<xsl:apply-templates select="n1:title" />
				<xsl:call-template name="medComments">
					<xsl:with-param select="." name="section" />
				</xsl:call-template>
				<xsl:call-template name="medDetails">
					<xsl:with-param select="." name="section" />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="n1:code[@code=$loincEncounterCode]">
				<xsl:apply-templates select="n1:title" />
				<xsl:call-template name="encounterDetails">
					<xsl:with-param select="." name="section" />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="n1:code[@code=$loincResultsCode]">
				<xsl:apply-templates select="n1:title" />
				<xsl:call-template name="resultsComments">
					<xsl:with-param select="." name="section" />
				</xsl:call-template>
				<xsl:call-template name="resultsDetails">
					<xsl:with-param select="." name="section" />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="n1:code[@code=$loincProceduresCode]">
				<xsl:apply-templates select="n1:title" />
				<xsl:call-template name="proceduresDetails">
					<xsl:with-param select="." name="section" />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="n1:code[@code=$loincImmunizationsCode]">
				<xsl:apply-templates select="n1:title" />
				<xsl:call-template name="immunizationsDetails">
					<xsl:with-param select="." name="section" />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="n1:code[@code=$loincVitalsCode]">
				<xsl:apply-templates select="n1:title" />
				<xsl:call-template name="vitalsDetails">
					<xsl:with-param select="." name="section" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<!-- temporary hold place so that unsupported modules not displayed -->
				<xsl:text />
			</xsl:otherwise>
		</xsl:choose>
		<xsl:apply-templates select="n1:component/n1:section" />
	</xsl:template>

	<!-- Encounter Detail Section -->
	<xsl:template name="encounterDetails">
		<xsl:param name="section" />
		<table>
			<thead>
				<tr>
					<th>
						<xsl:text>Date/Time</xsl:text>
						<xsl:if test="n1:entry/n1:encounter">
							<xsl:text> - Count (</xsl:text>
							<xsl:value-of select="count(n1:entry/n1:encounter)" />
							<xsl:text>)</xsl:text>
						</xsl:if>
					</th>
					<th>Encounter Type</th>
					<th>Encounter Description</th>
					<th>Reason</th>
					<th>Arrival</th>
					<th>Departure</th>
					<th>Provider</th>
					<th>Source</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="$section/n1:entry">
					<xsl:sort select="n1:encounter/n1:effectiveTime/n1:low/@value"
						order="descending" />
				</xsl:apply-templates>
			</tbody>
		</table>
		<br></br>
		<br></br>
	</xsl:template>

	<!-- Meds Detail Section -->
	<xsl:template name="medDetails">
		<xsl:param name="section" />
		<table>
			<thead>
				<tr>
					<th>
						<xsl:text>Medications</xsl:text>
						<xsl:if test="n1:entry/n1:substanceAdministration">
							<xsl:text> - Count (</xsl:text>
							<xsl:value-of select="count(n1:entry/n1:substanceAdministration)" />
							<xsl:text>)</xsl:text>
						</xsl:if>
					</th>
					<!--<th>Route</th> -->
					<!--<th>Interval</th> -->
					<th>Status</th>
					<th>Quantity</th>
					<th>Order Expiration</th>
					<th>Provider</th>
					<th>Prescription #</th>
					<th>Dispense Date</th>
					<th>Sig</th>
					<th>Source</th>
				</tr>
			</thead>
			<tbody>
				<xsl:choose>
					<xsl:when
						test="$section/n1:entry/n1:substanceAdministration/n1:effectiveTime/n1:high">
						<xsl:apply-templates select="$section/n1:entry">
							<xsl:sort
								select="$section/n1:entry/n1:substanceAdministration/n1:effectiveTime/n1:high/@value" />
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="$section/n1:entry">
							<xsl:sort
								select="$section/n1:entry/n1:substanceAdministration/n1:entryRelationship/n1:supply/n1:effectiveTime/@value" />
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
			</tbody>
		</table>
		<br></br>
		<br></br>
	</xsl:template>

	<!-- Problem Detail Section -->
	<xsl:template name="problemDetails">
		<xsl:param name="section" />
		<table>
			<thead>
				<tr>
					<th>
						<xsl:text>Problems</xsl:text>
						<xsl:if
							test="n1:entry/n1:act/n1:entryRelationship[@typeCode='SUBJ']/n1:observation">
							<xsl:text> - Count (</xsl:text>
							<xsl:value-of
								select="count(n1:entry/n1:act/n1:entryRelationship[@typeCode='SUBJ']/n1:observation)" />
							<xsl:text>)</xsl:text>
						</xsl:if>
					</th>
					<th>Status</th>
					<th>Problem Code</th>
					<th>Date of Onset</th>
					<th>Provider</th>
					<th>Source</th>
				</tr>

			</thead>
			<tbody>
				<xsl:choose>
					<xsl:when
						test="$section/n1:entry/n1:act/n1:entryRelationship/n1:observation/n1:effectiveTime/n1:low">
						<xsl:apply-templates select="$section/n1:entry">
							<xsl:sort
								select="n1:act/n1:entryRelationship/n1:observation/n1:effectiveTime/n1:low/@value"
								order="descending" />
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="$section/n1:entry">
							<xsl:sort select="n1:act/n1:effectiveTime/n1:low/@value"
								order="descending" />
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
			</tbody>
		</table>
		<br></br>
		<br></br>
	</xsl:template>

	<!-- Allergy Detail Section -->
	<xsl:template name="allergyDetails">
		<xsl:param name="section" />
		<table>
			<thead>
				<tr>
					<th>
						<xsl:text>Allergens</xsl:text>
						<xsl:if
							test="n1:entry/n1:act/n1:entryRelationship[@typeCode='SUBJ']/n1:observation">
							<xsl:text> - Count (</xsl:text>
							<xsl:value-of
								select="count(n1:entry/n1:act/n1:entryRelationship[@typeCode='SUBJ']/n1:observation)" />
							<xsl:text>)</xsl:text>
						</xsl:if>
					</th>
					<th>Verification Date</th>
					<th>Event Type</th>
					<!--<th>Product Free-Text</th> -->
					<!--<th>Product Coded</th> -->
					<th>Reaction</th>
					<!--<th>Reaction Free-Text</th> -->
					<!--<th>Reaction Coded</th> -->
					<th>Severity</th>
					<!--<th>Severity Free-Text</th> -->
					<!--<th>Severity Coded</th> -->
					<th>Source</th>
				</tr>
			</thead>
			<tbody>
				<xsl:choose>
					<xsl:when
						test="$section/n1:entry/n1:act/n1:entryRelationship/n1:observation/n1:effectiveTime/n1:low">
						<xsl:apply-templates select="$section/n1:entry">
							<xsl:sort
								select="n1:act/n1:entryRelationship/n1:observation/n1:effectiveTime/n1:low/@value"
								order="descending" />
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="$section/n1:entry">
							<xsl:sort select="n1:act/n1:effectiveTime/n1:low/@value"
								order="descending" />
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
			</tbody>
		</table>
		<br></br>
		<br></br>
	</xsl:template>

	<!-- (Lab)Results Detail Section -->
	<xsl:template name="resultsDetails">
		<xsl:param name="section" />
		<table>
			<thead>
				<tr>
					<th>
						<xsl:text>Date/Time</xsl:text>
						<xsl:if test="n1:entry/n1:observation">
							<xsl:text> - Count (</xsl:text>
							<xsl:value-of select="count(n1:entry/n1:observation)" />
							<xsl:text>)</xsl:text>
						</xsl:if>
					</th>
					<th>Result Type</th>
					<th>Source</th>
					<th>Result - Unit</th>
					<th>Interpretation</th>
					<th>Reference Range</th>
					<th>Status</th>
					<th>Comment</th>
				</tr>
			</thead>
			<tbody>
				<xsl:choose>
					<xsl:when test="n1:observation/n1:effectiveTime/@value">
						<xsl:apply-templates select="$section/n1:entry">
							<xsl:sort select="n1:observation/n1:effectiveTime/@value"
								order="descending" />
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="$section/n1:entry">
							<xsl:sort
								select="n1:organizer/n1:component/n1:observation/n1:effectiveTime/@value"
								order="descending" />
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
			</tbody>
		</table>
		<br></br>
		<br></br>
	</xsl:template>

	<!-- Procedures Detail Section -->
	<xsl:template name="proceduresDetails">
		<xsl:param name="section" />
		<table>
			<thead>
				<tr>
					<th>
						<xsl:text>Date/Time</xsl:text>
						<xsl:if test="n1:entry/n1:procedure/n1:code/@code">
							<xsl:text> - Count (</xsl:text>
							<xsl:value-of select="count(n1:entry/n1:procedure)" />
							<xsl:text>)</xsl:text>
						</xsl:if>
					</th>
					<th>Procedure Type</th>
					<th>Qualifiers</th>
					<th>Description</th>
					<th>Provider</th>
					<th>Source</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="$section/n1:entry">
					<xsl:sort select="n1:procedure/n1:effectiveTime/@value"
						order="descending" />
				</xsl:apply-templates>
			</tbody>
		</table>
		<br></br>
		<br></br>
	</xsl:template>

	<!-- Immunizations Detail Section -->
	<xsl:template name="immunizationsDetails">
		<xsl:param name="section" />
		<table>
			<thead>
				<tr>
					<th>Immunizations</th>
					<th>Series</th>
					<th>Date Issued</th>
					<th>Reaction</th>
					<th>Comments</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="$section/n1:entry">
				</xsl:apply-templates>
			</tbody>
		</table>
		<br></br>
		<br></br>
	</xsl:template>

	<!-- Vitals Detail Section -->
	<xsl:template name="vitalsDetails">
		<xsl:param name="section" />
		<table>
			<thead>
				<tr>
					<th>Date</th>
					<th>TEMP</th>
					<th>PULSE</th>
					<th>RESP</th>
					<th>BP</th>
					<th>Ht</th>
					<th>Wt</th>
					<th>POx</th>
					<th>Source</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="$section/n1:entry">
				</xsl:apply-templates>
			</tbody>
		</table>
		<br></br>
		<br></br>
	</xsl:template>

	<!-- entry processing -->
	<xsl:template match="n1:entry">
		<xsl:variable name="allergy-prob-Root"
			select="n1:act/n1:entryRelationship/n1:observation/n1:templateId/@root" />
		<xsl:variable name="med-imm-Root"
			select="n1:substanceAdministration/n1:templateId/@root" />
		<xsl:variable name="labs-Root" select="../n1:templateId/@root" />
		<xsl:choose>
			<xsl:when test="$allergy-prob-Root='2.16.840.1.113883.10.20.1.18'">
				<xsl:call-template name="allergyRow">
					<xsl:with-param name="row" select="." />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$allergy-prob-Root!='2.16.840.1.113883.10.20.1.18'">
				<xsl:call-template name="problemRow">
					<xsl:with-param name="row" select="." />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="n1:encounter">
				<xsl:call-template name="encRow">
					<xsl:with-param name="row" select="." />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$med-imm-Root='2.16.840.1.113883.3.88.11.83.13'">
				<xsl:call-template name="immunizationsRow">
					<xsl:with-param name="row" select="." />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$med-imm-Root!='2.16.840.1.113883.3.88.11.83.13'">
				<xsl:call-template name="medRow">
					<xsl:with-param name="row" select="." />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="n1:substanceAdministration">
				<xsl:call-template name="medRow">
					<xsl:with-param name="row" select="." />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$labs-Root='2.16.840.1.113883.10.20.1.14'">
				<xsl:call-template name="labsRow">
					<xsl:with-param name="row" select="." />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="n1:organizer">
				<xsl:call-template name="vitalsRow">
					<xsl:with-param name="row" select="." />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="n1:procedure">
				<xsl:call-template name="procedureRow">
					<xsl:with-param name="row" select="." />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise />
		</xsl:choose>
	</xsl:template>


	<!-- Encounter row entry -->
	<xsl:template name="encRow">
		<xsl:param name="row" />
		<tr>

			<!-- Encounter Date/Time -->
			<td>
				<xsl:choose>
					<xsl:when test="$row/n1:encounter/n1:effectiveTime/n1:low/@value">
						<xsl:call-template name="formatDate">
							<xsl:with-param name="date"
								select="$row/n1:encounter/n1:effectiveTime/n1:low/@value" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>

			<!-- Encounter Type -->
			<td>
				<xsl:choose>
					<xsl:when test="$row/n1:encounter">
						<xsl:call-template name="getEncounterType">
							<xsl:with-param name="encounter" select="$row/n1:encounter" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>

			<!-- Encounter Description -->

			<td>
				<xsl:variable name="encFreeText">
					<xsl:call-template name="getEncounterFreeText">
						<xsl:with-param name="encounter" select="$row/n1:encounter" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string-length($encFreeText)>1">
						<xsl:call-template name="flyoverTextSpan">
							<xsl:with-param name="data" select="$encFreeText" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>

			<!-- Encounter Reason -->
			<td>
				<xsl:variable name="encReason">
					<xsl:call-template name="getEncounterReason">
						<xsl:with-param name="encounter" select="$row/n1:encounter" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string-length($encReason)>1">
						<xsl:call-template name="flyoverSpan">
							<xsl:with-param name="data" select="$encReason" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>


			<!-- Encounter Arrival -->
			<td>
				<xsl:choose>
					<xsl:when test="$row/n1:encounter/n1:participant/n1:time/n1:low/@value">
						<xsl:call-template name="formatDateTime">
							<xsl:with-param name="date"
								select="$row/n1:encounter/n1:participant/n1:time/n1:low/@value" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>


			<!-- Encounter Departure -->
			<td>
				<xsl:choose>
					<xsl:when test="$row/n1:encounter/n1:participant/n1:time/n1:high/@value">
						<xsl:call-template name="formatDateTime">
							<xsl:with-param name="date"
								select="$row/n1:encounter/n1:participant/n1:time/n1:high/@value" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>


			<!-- Encounter Provider -->
			<td>
				<xsl:variable name="encProvider">
					<xsl:call-template name="getEncounterProvider">
						<xsl:with-param name="encounter" select="$row/n1:encounter" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string-length($encProvider)>1">
						<xsl:call-template name="flyoverSpan">
							<xsl:with-param name="data" select="$encProvider" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>

			</td>

			<!-- Encounter Source -->
			<td>
				<xsl:variable name="encSource">
					<xsl:call-template name="getEncounterSource">
						<xsl:with-param name="encounter" select="$row/n1:encounter" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string-length($encSource)>1">
						<xsl:call-template name="flyoverSpan">
							<xsl:with-param name="data" select="$encSource" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>

		</tr>
	</xsl:template>

	<!-- Medication Entry row -->
	<xsl:template name="medRow">
		<xsl:param name="row" />
		<tr>
			<!-- Name -->
			<td>
				<xsl:call-template name="flyoverSpan">
					<xsl:with-param name="data">
						<xsl:call-template name="getMedicationName">
							<xsl:with-param name="substanceAdmin"
								select="$row/n1:substanceAdministration" />
							<xsl:with-param name="row" select="$row" />
						</xsl:call-template>
					</xsl:with-param>
				</xsl:call-template>
			</td>

			<!-- Brand Name -->
			<!-- <td> <div style="overflow:hidden; white-space:nowrap"> <xsl:choose> 
				<xsl:when test="string-length($row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:name)>1"> 
				<xsl:call-template name="flyoverSpan"> <xsl:with-param name="data" select="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:name"/> 
				</xsl:call-template> </xsl:when> <xsl:otherwise> <xsl:call-template name="na"/> 
				</xsl:otherwise> </xsl:choose> </div> </td> -->

			<!-- Route -->
			<!-- <td> <div style="overflow:hidden; white-space:nowrap;"> <xsl:choose> 
				<xsl:when test="string-length($row//n1:substanceAdministration/n1:routeCode/@displayName)=0"> 
				<xsl:call-template name="na"/> </xsl:when> <xsl:otherwise> <xsl:value-of 
				select="$row/n1:substanceAdministration/n1:routeCode/@displayName"/> </xsl:otherwise> 
				</xsl:choose> </div> </td> -->

			<!-- Interval -->
			<!--<td> <div style="overflow:hidden; white-space:nowrap;"> <xsl:choose> --><!-- Test if Interval value is populated --><!-- <xsl:when test="string-length($row/n1:substanceAdministration/n1:effectiveTime/n1:period/@value)=0"> --><!-- Filler '-' if Interval null --><!-- <xsl:call-template name="na"/> </xsl:when> <xsl:otherwise> <xsl:value-of 
				select="$row/n1:substanceAdministration/n1:effectiveTime/n1:period/@value"/> 
				</xsl:otherwise> </xsl:choose> </div> </td> -->

			<!-- Status -->
			<td>
				<xsl:call-template name="medStatus">
					<xsl:with-param name="substanceAdmin"
						select="$row/n1:substanceAdministration" />
				</xsl:call-template>
			</td>

			<!-- Quantity -->
			<td>
				<xsl:call-template name="medQuantity">
					<xsl:with-param name="substanceAdmin"
						select="$row/n1:substanceAdministration" />
				</xsl:call-template>
			</td>

			<!-- Order Expiration Date/Time -->
			<td>
				<xsl:call-template name="medExpiretime">
					<xsl:with-param name="substanceAdmin"
						select="$row/n1:substanceAdministration" />
				</xsl:call-template>
			</td>

			<!-- Provider -->
			<td>
				<xsl:variable name="medProvider">
					<xsl:call-template name="getMedProvider">
						<xsl:with-param name="substanceAdmin"
							select="$row/n1:substanceAdministration" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string-length($medProvider)>1">
						<xsl:call-template name="flyoverSpan">
							<xsl:with-param name="data" select="$medProvider" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>

			<!-- Prescription ID (Nbr) -->
			<td>
				<xsl:variable name="rxNum">
					<xsl:call-template name="getRxNumString">
						<xsl:with-param name="substanceAdmin"
							select="$row/n1:substanceAdministration" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string-length($rxNum)>1">
						<xsl:value-of select="$rxNum" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>

			<!-- dispense time -->
			<td>
				<xsl:call-template name="medBegintime">
					<xsl:with-param name="row" select="$row" />
				</xsl:call-template>
			</td>

			<!-- Sig -->
			<td>
				<xsl:variable name="sig">
					<xsl:call-template name="getSig">
						<xsl:with-param name="substanceAdmin"
							select="$row/n1:substanceAdministration" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string-length($sig)>1">
						<xsl:call-template name="flyoverSpan">
							<xsl:with-param name="data" select="$sig" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>

			<!-- source -->
			<td>
				<xsl:call-template name="flyoverSpan">
					<xsl:with-param name="data"
						select="n1:substanceAdministration/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name" />
				</xsl:call-template>
			</td>
		</tr>
	</xsl:template>

	<!-- Problem entry row -->
	<xsl:template name="problemRow">
		<xsl:param name="row" />
		<xsl:variable name="rowData"
			select="$row/n1:act/n1:entryRelationship/n1:observation" />
		<xsl:variable name="ref"
			select="$row/n1:act/n1:entryRelationship/n1:observation/n1:text/n1:reference" />
		<tr>

			<!-- name -->
			<td>
				<xsl:call-template name="probName">
					<xsl:with-param name="ref" select="$ref" />
					<xsl:with-param name="rowData" select="$rowData" />
				</xsl:call-template>
			</td>

			<!-- status -->
			<td>
				<xsl:call-template name="probStatus">
					<xsl:with-param name="row" select="$row" />
				</xsl:call-template>
			</td>

			<!-- Problem Code -->
			<td>
				<xsl:call-template name="getProblemCode">
					<xsl:with-param name="rowData" select="$rowData" />
				</xsl:call-template>
			</td>

			<!-- problem effective date -->
			<td>
				<xsl:call-template name="probDate">
					<xsl:with-param name="row" select="$row" />
				</xsl:call-template>

			</td>

			<!-- provider -->
			<td>
				<xsl:variable name="provider" />
				<xsl:call-template name="getProblemProvider">
					<xsl:with-param name="performer" select="$row/n1:act/n1:performer" />
				</xsl:call-template>
				<xsl:if test="string-length($provider)>2">
					<xsl:call-template name="flyoverSpan">
						<xsl:with-param name="data" select="$provider" />
					</xsl:call-template>
				</xsl:if>
			</td>

			<!-- source -->
			<td>
				<xsl:variable name="source">
					<xsl:call-template name="getProblemSource">
						<xsl:with-param name="row" select="$row" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string-length($source)>1">
						<xsl:call-template name="flyoverSpan">
							<xsl:with-param name="data" select="$source" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>

	<!-- Allergy entry row -->
	<xsl:template name="allergyRow">
		<xsl:param name="row" />
		<xsl:variable name="observation"
			select="$row/n1:act/n1:entryRelationship/n1:observation" />
		<xsl:variable name="eR" select="$row/n1:act/n1:entryRelationship" />
		<tr>

			<!--Allergens -->
			<td>
				<xsl:variable name="allergen">
					<xsl:call-template name="getAllergen">
						<xsl:with-param name="observation" select="$observation" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string-length($allergen)>1">
						<xsl:call-template name="flyoverSpan">
							<xsl:with-param name="data" select="$allergen" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>

			<!--Verification Date -->
			<td>
				<xsl:choose>
					<xsl:when
						test="string-length($observation/n1:effectiveTime/n1:low/@value)=0">
						<xsl:call-template name="na" />
					</xsl:when>
					<xsl:when test="$observation/n1:effectiveTime/n1:low/@value">
						<xsl:call-template name="formatDate">
							<xsl:with-param name="date"
								select="$observation/n1:effectiveTime/n1:low/@value" />
						</xsl:call-template>
					</xsl:when>
				</xsl:choose>
			</td>

			<!--Event Type -->
			<td>
				<xsl:variable name="allergenType">
					<xsl:call-template name="getEventType">
						<xsl:with-param name="obs" select="$observation" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string-length($allergenType)>1">
						<xsl:call-template name="flyoverSpan">
							<xsl:with-param name="data" select="$allergenType" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>

			<!--Allergen Free-Text --><!-- <td style="overflow:hidden; white-space:nowrap; width:100px;"> <xsl:variable 
				name="allergenText"> <xsl:call-template name="getAllergenText"> <xsl:with-param 
				name="observation" select="$observation" /> </xsl:call-template> </xsl:variable> 
				<xsl:choose> <xsl:when test="string-length($allergenText)>1"> <xsl:call-template 
				name="flyoverSpan"> <xsl:with-param name="data" select="$allergenText"/> 
				</xsl:call-template> </xsl:when> <xsl:otherwise> <xsl:call-template name="na"/> 
				</xsl:otherwise> </xsl:choose> </td> -->

			<!--Product Coded --><!-- <td style="overflow:hidden; white-space:nowrap; width:100px;"> <xsl:call-template 
				name="na"/> </td> -->

			<!--Reaction -->
			<td>
				<xsl:variable name="results">
					<xsl:for-each select="$observation/n1:entryRelationship[@typeCode='MFST']">
						<xsl:variable name="result">
							<xsl:call-template name="getReactionValue">
								<xsl:with-param name="reaction"
									select="n1:observation/n1:text/n1:reference/@value" />
							</xsl:call-template>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="$result">
								<xsl:value-of select="$result" />
							</xsl:when>
						</xsl:choose>
					</xsl:for-each>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="$results">
						<xsl:call-template name="flyoverSpan">
							<xsl:with-param name="data" select="$results" />
						</xsl:call-template>
					</xsl:when>
				</xsl:choose>
			</td>

			<!--Reaction Free-Text -->
			<!--<td style="overflow:hidden; white-space:nowrap; width:100px;"> <xsl:variable 
				name="reactionText"> <xsl:call-template name="getReactionString"> <xsl:with-param 
				name="observation" select="$observation"/> </xsl:call-template> </xsl:variable> 
				<xsl:choose> <xsl:when test="string-length($reactionText)>1"> <xsl:value-of 
				select="$reactionText"/> </xsl:when> <xsl:otherwise> <xsl:call-template name="na"/> 
				</xsl:otherwise> </xsl:choose> </td> -->

			<!--Reaction Coded --><!-- <td style="overflow:hidden; white-space:nowrap; width:100px;"> <xsl:call-template 
				name="getReactionCoded"> <xsl:with-param name="eR" select="$eR"/> </xsl:call-template> 
				</td> -->

			<!--Severity -->
			<td>
				<xsl:variable name="severity">
					<xsl:call-template name="getSeverity">
						<xsl:with-param name="observation" select="$observation" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string-length($severity)>1">
						<xsl:value-of select="$severity" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>

			<!--Severity Free-Text --><!-- <td style="overflow:hidden; white-space:nowrap; width:100px;"> <xsl:call-template 
				name="na"/> </td> --><!--Severity Coded --><!-- <td style="overflow:hidden; white-space:nowrap; width:100px;"> <xsl:call-template 
				name="na"/> </td> -->

			<!--source -->
			<td>
				<xsl:variable name="source">
					<xsl:call-template name="getAllergySource">
						<xsl:with-param name="row" select="$row" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string-length($source)>1">
						<xsl:call-template name="flyoverSpan">
							<xsl:with-param name="data" select="$source" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>

	</xsl:template>

	<!-- Procedures row entry -->
	<xsl:template name="procedureRow">
		<xsl:param name="row" />
		<tr>

			<!-- Procedure Date/Time -->
			<td>
				<xsl:choose>
					<xsl:when test="$row/n1:procedure/n1:effectiveTime/@value">
						<xsl:call-template name="formatDate">
							<xsl:with-param name="date"
								select="$row/n1:procedure/n1:effectiveTime/@value" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="formatDate">
							<xsl:with-param name="date"
								select="$row/n1:procedure/n1:effectiveTime/n1:low/@value" />
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</td>

			<!-- Procedure Type -->
			<td>
				<xsl:variable name="type">
					<xsl:call-template name="getProcedureType">
						<xsl:with-param name="procedure" select="$row/n1:procedure" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string-length($type)>1">
						<xsl:call-template name="flyoverSpan">
							<xsl:with-param name="data" select="$type" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>


			<!-- Procedure Qualifier -->
			<td>
				<xsl:for-each select="$row/n1:procedure/n1:code/n1:qualifier">
					<xsl:variable name="qualifier">
						<xsl:call-template name="getProcedureQualifier">
							<xsl:with-param name="procedure" select="." />
						</xsl:call-template>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="string-length($qualifier)>1">
							<xsl:call-template name="flyoverSpan">
								<xsl:with-param name="data" select="$qualifier" />
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="na" />
						</xsl:otherwise>
					</xsl:choose>
					<br></br>
				</xsl:for-each>

			</td>



			<!-- Procedure Free Text Type -->

			<td>
				<xsl:variable name="procFreeText">
					<xsl:call-template name="getProcedureFreeText">
						<xsl:with-param name="procedure" select="$row/n1:procedure" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string-length($procFreeText)>1">
						<xsl:call-template name="flyoverTextSpan">
							<xsl:with-param name="data" select="$procFreeText" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>

			<!-- Procedure Provider -->
			<td>
				<xsl:variable name="procProvider">
					<xsl:call-template name="getProcedureProvider">
						<xsl:with-param name="procedure" select="$row/n1:procedure" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string-length($procProvider)>1">
						<xsl:call-template name="flyoverSpan">
							<xsl:with-param name="data" select="$procProvider" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>

			<!-- Procedure Source -->
			<td>
				<xsl:variable name="procSource">
					<xsl:call-template name="getProcedureSource">
						<xsl:with-param name="procedure" select="$row/n1:procedure" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string-length($procSource)>1">
						<xsl:call-template name="flyoverSpan">
							<xsl:with-param name="data" select="$procSource" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>

	<!-- labs entry row -->
	<xsl:template name="labsRow">
		<xsl:param name="row" />

		<xsl:choose>
			<xsl:when test="string-length($row/n1:organizer)!=0">
				<tr>
					<td>
						<xsl:choose>
							<xsl:when
								test="string-length($row/n1:organizer/n1:effectiveTime/@value)>0">
								<xsl:call-template name="formatDate">
									<xsl:with-param name="date"
										select="$row/n1:organizer/n1:effectiveTime/@value" />
								</xsl:call-template>
							</xsl:when>
							<xsl:when
								test="string-length($row/n1:organizer/n1:effectiveTime/n1:low/@value)>0">
								<xsl:call-template name="formatDate">
									<xsl:with-param name="date"
										select="$row/n1:organizer/n1:effectiveTime//n1:low/@value" />
								</xsl:call-template>
							</xsl:when>
							<xsl:when
								test="string-length($row/n1:organizer/n1:component/n1:procedure/n1:effectiveTime/@value)>0">
								<xsl:call-template name="formatDate">
									<xsl:with-param name="date"
										select="$row/n1:organizer/n1:component/n1:procedure/n1:effectiveTime/@value" />
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text> Not Available </xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</td>
					<td>
						<xsl:choose>
							<xsl:when test="string-length($row/n1:organizer/n1:code/@displayName)">
								<xsl:value-of select="$row/n1:organizer/n1:code/@displayName" />
							</xsl:when>
							<xsl:when
								test="string-length($row/n1:organizer/n1:code/n1:originalText)">
								<xsl:value-of select="$row/n1:organizer/n1:code/n1:originalText" />
							</xsl:when>
						</xsl:choose>

					</td>
					<!-- Source -->
					<td>
						<xsl:choose>
							<xsl:when
								test="$row/n1:organizer/n1:performer/n1:assignedEntity/n1:representedOrganization/n1:name">
								<xsl:value-of
									select="$row/n1:organizer/n1:performer/n1:assignedEntity/n1:representedOrganization/n1:name" />
							</xsl:when>
							<xsl:when
								test="$row/n1:organizer/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name">
								<xsl:value-of
									select="$row/n1:organizer/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name" />
							</xsl:when>
						</xsl:choose>
					</td>
					<td>
						<xsl:text>--</xsl:text>
					</td>
					<td>
						<xsl:text>--</xsl:text>
					</td>
					<td>
						<xsl:text>--</xsl:text>
					</td>
					<!-- Status -->
					<td>
						<xsl:value-of select="$row/n1:organizer/n1:statusCode/@code" />
					</td>
					<td>
						<xsl:text>--</xsl:text>
					</td>


				</tr>

				<xsl:for-each select="$row/n1:organizer/n1:component">
					<tr>
						<!-- Date -->
						<td>
							<xsl:text>--</xsl:text>
						</td>
						<!-- Test -->
						<td>
							<xsl:choose>
								<xsl:when
									test="string-length(n1:observation/n1:code/@displayName)!=0">
									<xsl:value-of select="n1:observation/n1:code/@displayName" />
								</xsl:when>
								<xsl:when
									test="string-length(n1:observation/n1:code/n1:originalText)!=0">
									<xsl:value-of select="n1:observation/n1:code/n1:originalText" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:variable name="labReference"
										select="n1:observation/n1:text/n1:reference/@value" />
									<xsl:value-of
										select="../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=$labReference]" />
								</xsl:otherwise>
							</xsl:choose>
						</td>

						<!-- Source -->
						<td>
							<xsl:text>--</xsl:text>
						</td>


						<!-- Result - Unit -->
						<td>
							<xsl:choose>
								<xsl:when test="string-length(n1:observation/n1:value/@value)!=0">
									<xsl:value-of select="n1:observation/n1:value/@value" />
									<xsl:if test="n1:observation/n1:value/@unit">
										<xsl:text> </xsl:text>
										<xsl:value-of select="n1:observation/n1:value/@unit" />
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:choose>
										<xsl:when
											test="string-length(n1:observation/n1:value/n1:high/@value)!=0">
											<xsl:value-of select="n1:observation/n1:value/n1:high/@value" />
											<xsl:if test="n1:observation/n1:value/n1:high/@unit">
												<xsl:text> </xsl:text>
												<xsl:value-of select="n1:observation/n1:value/n1:high/@unit" />
											</xsl:if>
										</xsl:when>
										<xsl:otherwise>
											<xsl:choose>
												<xsl:when
													test="string-length(n1:observation/n1:value/n1:low/@value)!=0">
													<xsl:value-of select="n1:observation/n1:value/n1:low/@value" />
													<xsl:if test="n1:observation/n1:value/n1:low/@unit">
														<xsl:text> </xsl:text>
														<xsl:value-of select="n1:observation/n1:value/n1:low/@unit" />
													</xsl:if>
												</xsl:when>
												<xsl:otherwise>
													<xsl:choose>
														<xsl:when test="string-length(n1:observation/n1:value)!=0">
															<xsl:value-of select="n1:observation/n1:value" />
														</xsl:when>
														<xsl:otherwise>
															<xsl:text>--</xsl:text>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<!-- Interpretation -->
						<td>
							<xsl:variable name="interpRef1"
								select="n1:observation/n1:interpretationCode/n1:originalText/n1:reference/@value" />
							<xsl:variable name="interpRef2"
								select="$row/../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=substring-after($interpRef1,'#')]" />

							<xsl:value-of
								select="n1:observation/n1:interpretationCode/n1:originalText" />
							<xsl:if
								test="string-length(n1:observation/n1:interpretationCode/n1:originalText)=0">
								<xsl:value-of
									select="n1:observation/n1:interpretationCode/@displayName" />
								<xsl:if
									test="string-length(n1:observation/n1:interpretationCode/@displayName)=0">
									<xsl:text>--</xsl:text>
								</xsl:if>
							</xsl:if>
							<xsl:if test="string-length($interpRef2)">
								<xsl:value-of select="$interpRef2" />
							</xsl:if>
						</td>
						<!-- Ref Range -->
						<td>
							<xsl:call-template name="flyoverSpan">
								<xsl:with-param name="data"
									select="n1:observation/n1:referenceRange/n1:observationRange/n1:text" />
							</xsl:call-template>
							<xsl:if
								test="string-length(n1:observation/n1:referenceRange/n1:observationRange/n1:text)=0">
								<xsl:text>--</xsl:text>
							</xsl:if>
						</td>
						<!-- Status -->
						<td>
							<xsl:value-of select="n1:observation/n1:statusCode/@code" />
						</td>
						<!-- Comment -->
						<td>
							<xsl:variable name="labCommentRef"
								select="n1:observation/n1:entryRelationship/n1:act/n1:text/n1:reference/@value" />
							<xsl:choose>
								<xsl:when test="$row/../n1:text/n1:content[@ID=$labCommentRef]">
									<xsl:call-template name="flyoverTextSpan">
										<xsl:with-param name="data"
											select="$row/../n1:text/n1:content[@ID=$labCommentRef]" />
									</xsl:call-template>
								</xsl:when>
								<xsl:when
									test="$row/../n1:text/n1:content[@ID=substring($labCommentRef,2)]">
									<xsl:call-template name="flyoverTextSpan">
										<xsl:with-param name="data"
											select="$row/../n1:text/n1:content[@ID=substring($labCommentRef,2)]" />
									</xsl:call-template>
								</xsl:when>
							</xsl:choose>



						</td>
					</tr>
				</xsl:for-each>

			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when
						test="string-length($row/n1:observation/n1:code/@displayName)!=0 or $row/n1:observation/n1:text/n1:reference/@value">
						<tr>
							<!-- Date -->
							<td>
								<xsl:choose>
									<xsl:when
										test="string-length($row/n1:observation/n1:effectiveTime/@value)=0">
										<xsl:text> Not Available </xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="formatDate">
											<xsl:with-param name="date"
												select="$row/n1:observation/n1:effectiveTime/@value" />
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<!-- Test -->
							<td>
								<xsl:choose>
									<xsl:when
										test="string-length($row/n1:observation/n1:code/@displayName)!=0">
										<xsl:value-of select="$row/n1:observation/n1:code/@displayName" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:variable name="labReference"
											select="$row/n1:observation/n1:text/n1:reference/@value" />
										<!-- <xsl:value-of select="../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=$labReference]"/> -->
										<xsl:value-of select="$row/n1:observation/n1:code/n1:originalText" />
									</xsl:otherwise>
								</xsl:choose>
							</td>


							<!-- Source -->
							<td>
								<xsl:value-of
									select="$row/n1:observation/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name" />
								<xsl:if
									test="string-length($row/n1:observation/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name)=0">
									<xsl:text>--</xsl:text>
								</xsl:if>
							</td>

							<!-- Result - Unit -->
							<td>
								<xsl:choose>
									<xsl:when
										test="string-length($row/n1:observation/n1:value/@value)!=0">
										<xsl:value-of select="$row/n1:observation/n1:value/@value" />
										<xsl:if test="$row/n1:observation/n1:value/@unit">
											<xsl:text> </xsl:text>
											<xsl:value-of select="$row/n1:observation/n1:value/@unit" />
										</xsl:if>
									</xsl:when>
									<xsl:otherwise>
										<xsl:choose>
											<xsl:when
												test="string-length($row/n1:observation/n1:value/n1:high/@value)!=0">
												<xsl:value-of select="$row/n1:observation/n1:value/n1:high/@value" />
												<xsl:if test="$row/n1:observation/n1:value/n1:high/@unit">
													<xsl:text> </xsl:text>
													<xsl:value-of select="$row/n1:observation/n1:value/n1:high/@unit" />
												</xsl:if>
											</xsl:when>
											<xsl:otherwise>
												<xsl:choose>
													<xsl:when
														test="string-length($row/n1:observation/n1:value/n1:low/@value)!=0">
														<xsl:value-of select="$row/n1:observation/n1:value/n1:low/@value" />
														<xsl:if test="$row/n1:observation/n1:value/n1:low/@unit">
															<xsl:text> </xsl:text>
															<xsl:value-of select="$row/n1:observation/n1:value/n1:low/@unit" />
														</xsl:if>
													</xsl:when>
													<xsl:otherwise>
														<xsl:choose>
															<xsl:when
																test="string-length($row/n1:observation/n1:value)!=0">
																<xsl:value-of select="$row/n1:observation/n1:value" />
															</xsl:when>
															<xsl:otherwise>
																<xsl:text>--</xsl:text>
															</xsl:otherwise>
														</xsl:choose>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<!-- Interpretation -->
							<td>
								<xsl:value-of
									select="$row/n1:observation/n1:interpretationCode/n1:originalText" />
								<xsl:if
									test="string-length($row/n1:observation/n1:interpretationCode/n1:originalText)=0">
									<xsl:value-of
										select="$row/n1:observation/n1:interpretationCode/@displayName" />
									<xsl:if
										test="string-length($row/n1:observation/n1:interpretationCode/@displayName)=0">
										<xsl:text>--</xsl:text>
									</xsl:if>
								</xsl:if>

							</td>
							<!-- Ref Range -->
							<td>
								<xsl:call-template name="flyoverSpan">
									<xsl:with-param name="data"
										select="$row/n1:observation/n1:referenceRange/n1:observationRange/n1:text" />
								</xsl:call-template>
								<xsl:if
									test="string-length($row/n1:observation/n1:referenceRange/n1:observationRange/n1:text)=0">
									<xsl:text>--</xsl:text>
								</xsl:if>
							</td>
							<!-- Status -->
							<td>
								<xsl:value-of select="$row/n1:observation/n1:statusCode/@code" />
							</td>
							<td>
								<xsl:text>--</xsl:text>
							</td>

						</tr>
					</xsl:when>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- immunization entry row -Backup of original template - Anand 07/01/11 
		<xsl:template name="immunizationsRow"> <xsl:param name="row"/> <xsl:variable 
		name="rowData" select="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial"/> 
		<xsl:variable name="rowSubj" select="$row/n1:substanceAdministration/n1:entryRelationship[@typeCode='SUBJ']/n1:observation"/> 
		<xsl:variable name="rowCause" select="$row/n1:substanceAdministration/n1:entryRelationship[@typeCode='CAUS']/n1:observation"/> 
		<tr> <td> <div style="overflow:hidden; white-space:nowrap; width:360px;"> 
		<xsl:variable name="immReference" select="$rowData/n1:code/n1:originalText/n1:reference/@value"/> 
		<xsl:choose> <xsl:when test="../n1:text/n1:content[@ID=starts-with($immReference,'#')]"> 
		<xsl:value-of select="../n1:text/n1:content[@ID=starts-with($immReference,'#')]"/> 
		<xsl:call-template name="flyoverSpan"> <xsl:with-param name="data" select="../n1:text/n1:content[@ID=substring($immReference,2)]"/> 
		</xsl:call-template> </xsl:when> <xsl:when test="../n1:text/n1:content[@ID=$immReference]"> 
		<xsl:value-of select="../n1:text/n1:content[@ID=$immReference]"/> <xsl:call-template 
		name="flyoverSpan"> <xsl:with-param name="data" select="../n1:text/n1:content[@ID=$immReference]"/> 
		</xsl:call-template> </xsl:when> </xsl:choose> <xsl:value-of select="../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=$immReference]"/> 
		</div> </td> series <td> <div style="overflow:hidden; white-space:nowrap; 
		width:60px;"> <xsl:call-template name="flyoverSpan"> <xsl:with-param name="data" 
		select="$rowSubj/n1:value"/> </xsl:call-template> </div> </td> -->
	<!-- immunization entry row -->
	<!-- Replaced original immunizationsRow Template - Anand 07/01/11 -->
	<xsl:template name="immunizationsRow">
		<xsl:param name="row" />
		<xsl:variable name="rowData"
			select="$row/n1:substanceAdministration/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial" />
		<xsl:variable name="rowSubj"
			select="$row/n1:substanceAdministration/n1:entryRelationship[@typeCode='SUBJ']/n1:observation" />
		<xsl:variable name="rowCause"
			select="$row/n1:substanceAdministration/n1:entryRelationship[@typeCode='CAUS']/n1:observation" />
		<tr>
			<!-- name -->
			<td>
				<xsl:variable name="immunization">
					<xsl:call-template name="getImmunization">
						<xsl:with-param name="manufacturedMaterial" select="$rowData" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string-length($immunization)>1">
						<xsl:call-template name="flyoverSpan">
							<xsl:with-param name="data" select="$immunization" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<!-- series -->
			<td>
				<xsl:variable name="immunizationSeries">
					<xsl:call-template name="getImmunizationSeries">
						<xsl:with-param name="manufacturedMaterial" select="$rowData" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string-length($immunizationSeries)>1">
						<xsl:call-template name="flyoverSpan">
							<xsl:with-param name="data" select="$immunizationSeries" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<!-- effective date -->
			<td>
				<xsl:choose>
					<xsl:when
						test="string-length($row/n1:substanceAdministration/n1:effectiveTime/@value)=0">
						<xsl:text>-- Not Available --</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="formatDate">
							<xsl:with-param name="date"
								select="$row/n1:substanceAdministration/n1:effectiveTime/@value" />
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<!-- reaction -->
			<td>
				<xsl:value-of select="rowCause/n1:id//@extension" />
				<xsl:variable name="reactionReference" select="$rowCause/n1:id/@extension" />
				<xsl:variable name="reaction"
					select="../n1:text/n1:content[@ID=$reactionReference]" />
				<xsl:call-template name="flyoverSpan">
					<xsl:with-param name="data" select="$reaction" />
				</xsl:call-template>
			</td>
			<!-- comments -->
			<td>
				<xsl:variable name="commentReference"
					select="$row/n1:substanceAdministration/n1:text/n1:reference/@value" />

				<xsl:choose>
					<xsl:when test="../n1:text/n1:content[@ID=$commentReference]">
						<xsl:call-template name="flyoverSpan">
							<xsl:with-param name="data"
								select="../n1:text/n1:content[@ID=$commentReference]" />
						</xsl:call-template>
					</xsl:when>
					<xsl:when
						test="../n1:text/n1:content[@ID=substring($commentReference,2)]">
						<xsl:call-template name="flyoverSpan">
							<xsl:with-param name="data"
								select="../n1:text/n1:content[@ID=substring($commentReference,2)]" />
						</xsl:call-template>
					</xsl:when>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>

	<!-- vitals entry row -->
	<xsl:template name="vitalsRow">
		<xsl:param name="row" />
		<xsl:variable name="rowData"
			select="$row/n1:organizer/n1:component/n1:observation" />
		<xsl:variable name="height"
			select="$row/n1:organizer/n1:component/n1:observation/n1:code[@code='8302-2']/.." />
		<xsl:variable name="weight"
			select="$row/n1:organizer/n1:component/n1:observation/n1:code[@code='29463-7']/.." />
		<xsl:variable name="weight1"
			select="$row/n1:organizer/n1:component/n1:observation/n1:code[@code='3141-9']/.." />
		<xsl:variable name="systolic"
			select="$row/n1:organizer/n1:component/n1:observation/n1:code[@code='8480-6']/.." />
		<xsl:variable name="diastolic"
			select="$row/n1:organizer/n1:component/n1:observation/n1:code[@code='8462-4']/.." />
		<xsl:variable name="temp"
			select="$row/n1:organizer/n1:component/n1:observation/n1:code[@code='8310-5']/.." />
		<xsl:variable name="pulse"
			select="$row/n1:organizer/n1:component/n1:observation/n1:code[@code='8867-4']/.." />
		<xsl:variable name="resp"
			select="$row/n1:organizer/n1:component/n1:observation/n1:code[@code='9279-1']/.." />
		<xsl:variable name="pox"
			select="$row/n1:organizer/n1:component/n1:observation/n1:code[@code='2710-2']/.." />
		<tr>
			<!-- observation text -->
			<!-- problem effective date -->
			<td>
				<xsl:choose>
					<xsl:when test="string-length($rowData/n1:effectiveTime/@value)=0">
						<xsl:text>-- Not Available --</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="formatDate">
							<xsl:with-param name="date"
								select="$rowData/n1:effectiveTime/@value" />
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<!-- temp -->
			<td>
				<xsl:value-of select="$temp/n1:value/@value" />
				<xsl:value-of select="$temp/n1:value/@unit" />
			</td>
			<!-- pulse -->
			<td>
				<xsl:value-of select="$pulse/n1:value/@value" />
			</td>
			<!-- resp -->
			<td>
				<xsl:value-of select="$resp/n1:value/@value" />
			</td>
			<!-- BP systolic / diastolic -->
			<td>
				<xsl:value-of select="$systolic/n1:value/@value" />
				/
				<xsl:value-of select="$diastolic/n1:value/@value" />
			</td>
			<!-- height -->
			<td>
				<xsl:value-of select="$height/n1:value/@value" />
				<xsl:value-of select="$height/n1:value/@unit" />
			</td>
			<!-- weight -->
			<td>
				<xsl:if test="$weight">
					<xsl:value-of select="$weight/n1:value/@value" />
					<xsl:value-of select="$weight/n1:value/@unit" />
				</xsl:if>
				<xsl:if test="$weight1">
					<xsl:value-of select="$weight1/n1:value/@value" />
					<xsl:value-of select="$weight1/n1:value/@unit" />
				</xsl:if>
			</td>
			<!-- pox -->
			<td>
				<xsl:value-of select="$pox/n1:value/@value" />
			</td>
			<!-- source -->
			<td>
				<xsl:value-of
					select="$row/n1:organizer/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name" />
			</td>
		</tr>
	</xsl:template>

	<!-- flyover -->
	<xsl:template name="flyoverSpan">
		<xsl:param name="data" />
		<xsl:choose>
			<xsl:when test="$data">
				<span onmouseover='DisplayTip(this,25,-50)'>
					<xsl:value-of select="$data" />
				</span>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- flyover Free Text -->
	<xsl:template name="flyoverTextSpan">
		<xsl:param name="data" />
		<xsl:choose>
			<xsl:when test="$data">
				<span onmouseover='DisplayText(this,25,-50)'>
					<xsl:value-of select="$data" />
				</span>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>


	<!-- Title -->
	<xsl:template match="n1:title">
		<h3>
			<span style="font-weight:bold;">
				<a name="{generate-id(.)}" href="#toc">
					<xsl:value-of select="." />
				</a>
			</span>
		</h3>
	</xsl:template>

	<!-- Text -->
	<xsl:template match="n1:text">
		<xsl:apply-templates />
	</xsl:template>

	<!-- Bottomline -->

	<xsl:template name="bottomline">
		<p>
			<b>
				<xsl:text>Electronically generated: </xsl:text>
			</b>
			<xsl:call-template name="getName">
				<xsl:with-param name="name"
					select="/n1:ClinicalDocument/n1:legalAuthenticator/n1:assignedEntity/n1:representedOrganization/n1:name" />
			</xsl:call-template>
			<xsl:text> on </xsl:text>
			<xsl:choose>
				<xsl:when
					test="string-length(/n1:ClinicalDocument/n1:effectiveTime/@value)=0">
					<xsl:call-template name="na" />
				</xsl:when>
				<xsl:when
					test="starts-with(/n1:ClinicalDocument/n1:effectiveTime/@value,' ')">
					<xsl:call-template name="na" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="formatDateFull">
						<xsl:with-param name="date"
							select="/n1:ClinicalDocument/n1:effectiveTime/@value" />
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>

		</p>
	</xsl:template>

	<xsl:template
		match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.11']/n1:text/n1:table/n1:tbody">
		<xsl:apply-templates>
			<xsl:sort select="n1:td[3]" order="descending" />
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template
		match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.8']/n1:text/n1:table/n1:tbody">
		<xsl:apply-templates>
			<xsl:sort select="n1:td[5]" order="descending" />
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template
		match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.16' or n1:templateId/@root='2.16.840.1.113883.10.20.1.14' or n1:templateId/@root='2.16.840.1.113883.10.20.1.6' or n1:templateId/@root='2.16.840.1.113883.10.20.1.3']/n1:text/n1:table/n1:tbody">
		<xsl:apply-templates>
			<xsl:sort select="n1:td[2]" order="descending" />
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template
		match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.11']/n1:text/n1:table/n1:tbody/n1:tr/n1:td[3]">
		<td>
			<xsl:call-template name="formatDate">
				<xsl:with-param name="date" select="text()" />
			</xsl:call-template>
		</td>
	</xsl:template>

	<xsl:template
		match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.8']/n1:text/n1:table/n1:tbody/n1:tr/n1:td[5]">
		<td>
			<xsl:call-template name="formatDate">
				<xsl:with-param name="date" select="text()" />
			</xsl:call-template>
		</td>
	</xsl:template>

	<xsl:template
		match="n1:component/n1:section[n1:templateId/@root='2.16.840.1.113883.10.20.1.16' or n1:templateId/@root='2.16.840.1.113883.10.20.1.14' or n1:templateId/@root='2.16.840.1.113883.10.20.1.6' or n1:templateId/@root='2.16.840.1.113883.10.20.1.3']/n1:text/n1:table/n1:tbody/n1:tr/n1:td[2]">
		<td>
			<xsl:call-template name="formatDate">
				<xsl:with-param name="date"
					select="concat(substring(text(),1,4),substring(text(),6,2),substring(text(),9,2))" />
			</xsl:call-template>
		</td>
	</xsl:template>

	<xsl:template match="n1:languageCommunication">
		<xsl:variable name="langCode" select="substring(n1:languageCode/@code,1,2)" />
		<xsl:choose>
			<xsl:when test="string-length($langCode)=0">
			</xsl:when>
			<xsl:when test="$langCode='en'">
				<li>
					<xsl:text>English</xsl:text>
				</li>
			</xsl:when>
			<xsl:when test="$langCode='es'">
				<li>
					<xsl:text>Spanish</xsl:text>
				</li>
			</xsl:when>
			<xsl:otherwise>
				<li>
					<xsl:value-of select="n1:languageCode/@code" />
				</li>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="documentTitle">
		<xsl:param name="root" />

		<xsl:choose>
			<xsl:when
				test="$root/n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:name and string-length($root/n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:name)>0">
				<xsl:value-of
					select="$root/n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:name" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of
					select="$root/n1:author[1]/n1:assignedAuthor/n1:representedOrganization/n1:name" />
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>

	<!-- free text processing -->

	<xsl:template name="freeText">
		<xsl:param name="text" />

	</xsl:template>


	<xsl:template name="getMedName">
		<xsl:param name="substanceAdmin" />
		<xsl:variable name="ref1"
			select="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code/n1:originalText/n1:reference/@value" />
		<xsl:if test="$substanceAdmin">
			<xsl:choose>
				<xsl:when test="../n1:text/n1:list/n1:item/n1:content[@ID=$ref1]">
					<xsl:value-of
						select="$row/../n1:text/n1:list/n1:item/n1:content[@ID=$ref1]" />
				</xsl:when>
				<xsl:when test="../n1:text/n1:content[@ID=substring-after($ref1,'#')]">
					<xsl:value-of
						select="../n1:text/n1:content[@ID=substring-after($ref1,'#')]" />
				</xsl:when>
				<xsl:when
					test="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code/n1:originalText">
					<xsl:value-of
						select="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code/n1:originalText" />
				</xsl:when>
				<xsl:when
					test="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code/@displayName">
					<xsl:value-of
						select="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code/@displayName" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of
						select="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:name" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="medStatus">
		<xsl:param name="substanceAdmin" />
		<xsl:variable name="status">
			<xsl:call-template name="getMedStatusString">
				<xsl:with-param name="substanceAdmin" select="$substanceAdmin" />
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="string-length($status)&gt;0">
				<xsl:value-of select="$status" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="medQuantity">
		<xsl:param name="substanceAdmin" />
		<xsl:variable name="qtyString">
			<xsl:call-template name="getMedQuantityString">
				<xsl:with-param name="substanceAdmin" select="$substanceAdmin" />
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="string-length($qtyString)>0">
				<xsl:value-of select="$qtyString" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="medBegintime">
		<xsl:param name="row" />
		<xsl:variable name="medBeginString">
			<xsl:call-template name="medDateBeginString">
				<xsl:with-param name="substanceAdmin"
					select="$row/n1:substanceAdministration" />
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="string-length($medBeginString)&gt;1">
				<xsl:call-template name="formatDate">
					<xsl:with-param name="date" select="$medBeginString" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="medExpiretime">
		<xsl:param name="substanceAdmin" />
		<xsl:variable name="medExpireString">
			<xsl:call-template name="medExpireDateString">
				<xsl:with-param name="substanceAdmin" select="$substanceAdmin" />
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="string-length($medExpireString)>1">
				<xsl:call-template name="formatDate">
					<xsl:with-param name="date" select="$medExpireString" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="probName">
		<xsl:param name="ref" />
		<xsl:param name="rowData" />
		<xsl:variable name="probReference1" select="$ref/@value" />
		<!--<xsl:variable name="probReference2" select="$ref/@value"/> -->
		<xsl:choose>
			<xsl:when test="../n1:text/n1:paragraph[@ID=$probReference1]">
				<xsl:value-of select="../n1:text/n1:paragraph[@ID=$probReference1]" />
			</xsl:when>

			<xsl:when
				test="../n1:text/n1:paragraph[@ID=substring-after($probReference1,'#')]">
				<xsl:value-of
					select="../n1:text/n1:paragraph[@ID=substring-after($probReference1,'#')]" />
			</xsl:when>

			<!--DoD -->
			<xsl:when
				test="../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=substring-after($probReference1,'#')]">
				<xsl:value-of
					select="../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=substring-after($probReference1,'#')]" />
			</xsl:when>

			<xsl:when
				test="../n1:text/n1:table/n1:tbody/n1:tr/n1:td[@ID=substring-after($probReference1,'#')]">
				<xsl:value-of
					select="../n1:text/n1:table/n1:tbody/n1:tr/n1:td[@ID=substring-after($probReference1,'#')]" />
			</xsl:when>

			<xsl:when test="../n1:text/n1:content[@ID=$probReference1]">
				<xsl:value-of select="../n1:text/n1:content[@ID=$probReference1]" />
			</xsl:when>
			<xsl:when
				test="../n1:text/n1:content[@ID=substring-after($probReference1,'#')]">
				<xsl:value-of
					select="../n1:text/n1:content[@ID=substring-after($probReference1,'#')]" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="probStatus">
		<xsl:param name="row" />
		<xsl:choose>
			<xsl:when
				test="$row/n1:act/n1:entryRelationship[@typeCode='SUBJ']/n1:observation/n1:entryRelationship[@typeCode='REFR']/n1:observation/n1:value/@displayName">
				<xsl:value-of
					select="$row/n1:act/n1:entryRelationship[@typeCode='SUBJ']/n1:observation/n1:entryRelationship[@typeCode='REFR']/n1:observation/n1:value/@displayName" />
			</xsl:when>
			<xsl:when
				test="$row/n1:act/n1:entryRelationship[@typeCode='REFR']/n1:observation/n1:value/@displayName">
				<xsl:value-of
					select="$row/n1:act/n1:entryRelationship[@typeCode='REFR']/n1:observation/n1:value/@displayName" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="probDate">
		<xsl:param name="row" />
		<xsl:variable name="rawDate">
			<xsl:call-template name="getProblemOnsetDateString">
				<xsl:with-param name="act" select="$row/n1:act" />
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="string-length($rawDate)>1">
				<xsl:call-template name="formatDate">
					<xsl:with-param name="date" select="$rawDate" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="allergyType">
		<xsl:param name="row" />
		<xsl:choose>
			<xsl:when test="$row/n1:code">
				<xsl:choose>
					<xsl:when test="$row/n1:code/n1:originalText">
						<xsl:value-of select="$row/n1:code/n1:originalText" />
					</xsl:when>
					<xsl:when test="$row/n1:code/@displayName">
						<xsl:value-of select="$row/n1:code/@displayName" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="na" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="allergySource">
		<xsl:param name="row" />
		<xsl:param name="isPlaintext" select="boolean(true)" />
		<xsl:choose>
			<xsl:when
				test="$row/n1:act/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name">
				<xsl:value-of
					select="$row/n1:act/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="getCreatedOnDate">
		<xsl:choose>
			<xsl:when
				test="string-length(/n1:ClinicalDocument/n1:effectiveTime/@value)=0">
				<xsl:call-template name="na" />
			</xsl:when>
			<xsl:when
				test="starts-with(/n1:ClinicalDocument/n1:effectiveTime/@value,' ')">
				<xsl:call-template name="na" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="formatDateFull">
					<xsl:with-param name="date"
						select="/n1:ClinicalDocument/n1:effectiveTime/@value" />
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="getGenderString">
		<xsl:choose>
			<xsl:when
				test="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:administrativeGenderCode/@code">
				<xsl:value-of
					select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:administrativeGenderCode/@code" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="genderString">
					<xsl:with-param name="sex"
						select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole/n1:patient/n1:administrativeGenderCode/n1:originalText" />
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="getReactionString">
		<xsl:param name="observation" />
		<!--<xsl:for-each select="$observation/n1:entryRelationship[@inversionInd='true' 
			and @typeCode='MFST']"> -->
		<xsl:for-each select="$observation/n1:entryRelationship[@typeCode='MFST']">
			<!--<xsl:variable name="reactionReference" select="substring-after(n1:observation/n1:text/n1:reference/@value,'#')"/> -->
			<xsl:variable name="reactionReference"
				select="n1:observation/n1:text/n1:reference/@value" />
			<xsl:variable name="reactionValue"
				select="/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component/n1:section/n1:text/n1:content[@ID=$reactionReference]" />
			<xsl:variable name="reactionValue2"
				select="/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component/n1:section/n1:text/n1:content[@ID=substring-after($reactionReference,'#')]" />
			<xsl:variable name="reaction"
				select="../../../../../n1:text/n1:content[@ID=$reactionReference]" />
			<xsl:if test="string-length($reactionValue)>1">
				<xsl:if test="position()>1">
					<xsl:text>, </xsl:text>
				</xsl:if>
				<xsl:value-of select="$reactionValue" />
			</xsl:if>
			<xsl:if test="string-length($reactionValue2)>1">
				<xsl:if test="position()>1">
					<xsl:text>, </xsl:text>
				</xsl:if>
				<xsl:value-of select="$reactionValue2" />
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="getSeverityString">
		<xsl:param name="observation" />
		<xsl:if test="$observation">
			<xsl:variable name="sevrReference"
				select="substring-after($observation/n1:entryRelationship/n1:observation/n1:entryRelationship/n1:observation/n1:text/n1:reference/@value,'#')" />
			<xsl:variable name="severity"
				select="$observation/../../../../n1:text/n1:content[@ID=$sevrReference]" />
			<xsl:if test="string-length($severity)>1">
				<xsl:value-of select="$severity" />
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getAllergyType">
		<xsl:param name="observation" />
		<xsl:choose>
			<xsl:when test="$observation/n1:code/@displayName">
				<xsl:value-of select="observation/n1:code/@displayName" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="getSeverityText">
		<xsl:param name="observation" />
		<xsl:if test="$observation">
			<xsl:variable name="sevrReference"
				select="substring-after($observation/n1:entryRelationship/n1:observation/n1:entryRelationship/n1:observation/n1:text/n1:reference/@value,'#')" />
			<xsl:variable name="severity"
				select="$observation/../../../../n1:text/n1:content[@ID=$sevrReference]" />
			<xsl:if test="string-length($severity)>1">
				<xsl:value-of select="$severity" />
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getProblemProviderName">
		<xsl:param name="act" />
		<xsl:if test="$act">
			<xsl:choose>
				<xsl:when
					test="$act/n1:entryRelationship/n1:observation/n1:entryRelationship/n1:observation[@classCode='OBS' and @moodCode='EVN']/n1:performer/n1:assignedEntity/n1:assignedPerson/n1:name">
					<xsl:value-of
						select="$act/n1:entryRelationship/n1:observation/n1:entryRelationship/n1:observation[@classCode='OBS' and @moodCode='EVN']/n1:performer/n1:assignedEntity/n1:assignedPerson/n1:name" />
				</xsl:when>
				<xsl:when
					test="$act/n1:performer/n1:assignedEntity/n1:assignedPerson/n1:name">
					<xsl:value-of
						select="$act/n1:performer/n1:assignedEntity/n1:assignedPerson/n1:name" />
				</xsl:when>
				<xsl:when test="$act/n1:performer/n1:assignedEntity/n1:id/@extension">
					<xsl:variable name="providerId"
						select="$act/n1:performer/n1:assignedEntity/n1:id/@extension" />
					<xsl:value-of
						select="/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:performer/n1:assignedEntity/n1:id[@extension=$providerId]/@assigningAuthorityName" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of
						select="$act/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getResultValue">
		<xsl:param name="observation" />
		<xsl:if test="$observation">
			<xsl:choose>
				<xsl:when test="$observation/n1:value/@value">
					<xsl:value-of select="$observation/n1:value/@value" />
					<xsl:if test="$observation/n1:value/@unit">
						<xsl:text> </xsl:text>
						<xsl:value-of select="$observation/n1:value/@unit" />
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="na" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getResultValueDetail">
		<xsl:param name="observation" />
		<xsl:if test="$observation">
			<xsl:choose>
				<xsl:when test="$observation/n1:value/@value">
					<xsl:value-of select="$observation/n1:value/@value" />
					<xsl:if test="$observation/n1:value/@unit">
						<xsl:text> </xsl:text>
						<xsl:value-of select="$observation/n1:value/@unit" />
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="na" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getFlag">
		<xsl:param name="interpretation" />
		<xsl:choose>
			<xsl:when test="$interpretation/@displayName">
				<xsl:value-of select="$interpretation/@displayName" />
			</xsl:when>
			<xsl:when test="$interpretation/n1:originalText">
				<xsl:value-of select="$interpretation/n1:originalText" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="getResultUnit">
		<xsl:param name="observation" />
		<xsl:if test="$observation">
			<xsl:choose>
				<xsl:when test="$observation/n1:value/@unit">
					<xsl:value-of select="$observation/n1:value/@unit" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="na" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="facilityProperty">
		<xsl:variable name="root" select="/n1:ClinicalDocument" />
		<Facility>
			<xsl:choose>
				<xsl:when
					test="$root/n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:name and string-length($root/n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:name)>0">
					<xsl:value-of
						select="$root/n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:name" />
				</xsl:when>

				<xsl:when
					test="$root/n1:documentationOf/n1:serviceEvent/n1:performer/n1:assignedEntity/n1:representedOrganization/n1:name and string-length($root/n1:documentationOf/n1:serviceEvent/n1:performer/n1:assignedEntity/n1:representedOrganization/n1:name)>0">
					<xsl:value-of
						select="$root/n1:documentationOf/n1:serviceEvent/n1:performer/n1:assignedEntity/n1:representedOrganization/n1:name" />
				</xsl:when>
				<xsl:otherwise />
			</xsl:choose>
			<!--xsl:if test="n1:act/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name"> 
				<xsl:text> / </xsl:text> <xsl:value-of select="n1:act/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name"/> 
				</xsl:if -->
		</Facility>

	</xsl:template>

	<xsl:template name="genderString">
		<xsl:param name="sex" select="'unknown'" />
		<xsl:choose>
			<xsl:when test="$sex='M' or $sex='m' or $sex='Male' or $sex='male'">
				<xsl:text>Male</xsl:text>
			</xsl:when>
			<xsl:when test="$sex='F' or $sex='f' or $sex='Female' or $sex='female'">
				<xsl:text>Female</xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:template>



	<xsl:template name="getImmunization">
		<xsl:param name="manufacturedMaterial" />

		<xsl:variable name="immReference">
			<xsl:choose>
				<xsl:when
					test="starts-with($manufacturedMaterial/n1:code/n1:originalText/n1:reference/@value,'#')">
					<xsl:value-of
						select="substring($manufacturedMaterial/n1:code/n1:originalText/n1:reference/@value,2)" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of
						select="$manufacturedMaterial/n1:code/n1:originalText/n1:reference/@value" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:choose>
			<xsl:when test="$immReference">
				<xsl:choose>
					<xsl:when
						test="../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=$immReference]">
						<xsl:value-of
							select="../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=$immReference]" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:if
							test="../n1:text/n1:table/n1:tbody/n1:tr/n1:td[@ID=$immReference]">
							<xsl:value-of
								select="../n1:text/n1:table/n1:tbody/n1:tr/n1:td[@ID=$immReference]" />
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$manufacturedMaterial/n1:name" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="getImmunizationSeries">
		<xsl:param name="manufacturedMaterial" />

		<xsl:value-of select="$manufacturedMaterial/n1:lotNumberText" />

	</xsl:template>

	<xsl:template name="getAllergen">
		<xsl:param name="observation" />
		<xsl:if test="$observation">
			<xsl:choose>
				<xsl:when
					test="string-length($observation/n1:participant/n1:participantRole/n1:playingEntity/n1:name)>0">
					<xsl:value-of
						select="$observation/n1:participant/n1:participantRole/n1:playingEntity/n1:name" />
				</xsl:when>
				<xsl:when
					test="$observation/n1:participant/n1:participantRole/n1:playingEntity/n1:code/n1:originalText/n1:reference/@value">
					<xsl:variable name="reactionNameRef1"
						select="$observation/n1:participant/n1:participantRole/n1:playingEntity/n1:code/n1:originalText/n1:reference/@value" />
					<xsl:variable name="reactionNameRef2"
						select="substring($observation/n1:participant/n1:participantRole/n1:playingEntity/n1:code/n1:originalText/n1:reference/@value,2)" />
					<xsl:if
						test="../n1:text/n1:table/n1:tbody/n1:tr/n1:td[@ID=$reactionNameRef1]">
						<xsl:value-of
							select="../n1:text/n1:table/n1:tbody/n1:tr/n1:td[@ID=$reactionNameRef1]" />
					</xsl:if>
					<xsl:if
						test="../n1:text/n1:table/n1:tbody/n1:tr/n1:td[@ID=$reactionNameRef2]">
						<xsl:value-of
							select="../n1:text/n1:table/n1:tbody/n1:tr/n1:td[@ID=$reactionNameRef2]" />
					</xsl:if>
					<xsl:if
						test="../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=$reactionNameRef1]">
						<xsl:value-of
							select="../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=$reactionNameRef1]" />
					</xsl:if>
					<xsl:if
						test="../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=$reactionNameRef2]">
						<xsl:value-of
							select="../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=$reactionNameRef2]" />
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of
						select="$observation/n1:participant[@typeCode='CSM']/n1:participantRole[@classCode='MANU']/n1:playingEntity[@classCode='MMAT']" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getAllergenText">
		<xsl:param name="observation" />
		<xsl:if test="$observation">
			<xsl:choose>
				<xsl:when
					test="$observation/n1:participant/n1:participantRole/n1:playingEntity/n1:name or $observation/n1:participant[@typeCode='CSM']/n1:participantRole[@classCode='MANU']/n1:playingEntity[@classCode='MMAT']/@name">
					<xsl:value-of
						select="$observation/n1:participant/n1:participantRole/n1:playingEntity/n1:name" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of
						select="$observation/participant[@typeCode='CSM']/n1:participantRole[@classCode='MANU']/n1:playingEntity[@classCode='MMAT']/@name" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getEventType">
		<xsl:param name="obs" />
		<xsl:if test="$obs">
			<xsl:choose>
				<xsl:when test="string-length($obs/n1:code/n1:displayName)>0">
					<xsl:value-of select="$obs/n1:code/n1:displayName" />
				</xsl:when>
				<xsl:when test="string-length($obs/n1:code/@displayName)>0">
					<xsl:value-of select="$obs/n1:code/@displayName" />
				</xsl:when>
				<xsl:when test="$obs/n1:code/n1:originalText/n1:reference/@value">
					<xsl:variable name="typeNameRef1"
						select="$obs/n1:code/n1:originalText/n1:reference/@value" />
					<xsl:variable name="typeNameRef2"
						select="substring($obs/n1:code/n1:originalText/n1:reference/@value,2)" />
					<xsl:if
						test="../n1:text/n1:table/n1:tbody/n1:tr/n1:td[@ID=$typeNameRef1]">
						<xsl:value-of
							select="../n1:text/n1:table/n1:tbody/n1:tr/n1:td[@ID=$typeNameRef1]" />
					</xsl:if>
					<xsl:if
						test="../n1:text/n1:table/n1:tbody/n1:tr/n1:td[@ID=$typeNameRef2]">
						<xsl:value-of
							select="../n1:text/n1:table/n1:tbody/n1:tr/n1:td[@ID=$typeNameRef2]" />
					</xsl:if>
					<xsl:if
						test="../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=$typeNameRef1]">
						<xsl:value-of
							select="../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=$typeNameRef1]" />
					</xsl:if>
					<xsl:if
						test="../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=$typeNameRef2]">
						<xsl:value-of
							select="../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=$typeNameRef2]" />
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="na" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>


	</xsl:template>

	<!--<xsl:template name="getReaction"> <xsl:param name="reaction"/> <xsl:variable 
		name="reactionReference" select="$reaction"/> <xsl:choose> <xsl:when test="/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component/n1:section/n1:text/n1:content[@ID=$reactionReference]"> 
		<xsl:value-of select="/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component/n1:section/n1:text/n1:content[@ID=$reactionReference]"/> 
		</xsl:when> <xsl:when test="starts-with($reactionReference,'#')"> <xsl:variable 
		name="reactionReference1" select="substring($reactionReference,2)"/> <xsl:value-of 
		select="/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component/n1:section/n1:text/n1:content[@ID=$reactionReference1]"/> 
		</xsl:when> </xsl:choose> </xsl:template> -->

	<xsl:template name="getReactionValue">
		<xsl:param name="reaction" />
		<xsl:variable name="reactionReference" select="$reaction" />
		<xsl:variable name="reactionValue"
			select="/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component/n1:section/n1:text/n1:content[@ID=$reactionReference]" />
		<xsl:variable name="reactionValue2"
			select="/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component/n1:section/n1:text/n1:content[@ID=substring-after($reactionReference,'#')]" />
		<xsl:variable name="reactionValue3"
			select="/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component/n1:section/n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=$reactionReference]" />
		<xsl:variable name="reactionValue4"
			select="/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component/n1:section/n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=substring-after($reactionReference,'#')]" />
		<xsl:variable name="reactionValue5"
			select="/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component/n1:section/n1:text/n1:table/n1:tbody/n1:tr/n1:td[@ID=$reactionReference]" />
		<xsl:variable name="reactionValue6"
			select="/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component/n1:section/n1:text/n1:table/n1:tbody/n1:tr/n1:td[@ID=substring-after($reactionReference,'#')]" />
		<!--<xsl:value-of select="$reactionValue"/> -->
		<xsl:if test="string-length($reactionValue)>1">
			<xsl:if test="position()>1">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:value-of select="$reactionValue" />
		</xsl:if>
		<xsl:if test="string-length($reactionValue2)>1">
			<xsl:if test="position()>1">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:value-of select="$reactionValue2" />
		</xsl:if>
		<xsl:if test="string-length($reactionValue3)>1">
			<xsl:if test="position()>1">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:value-of select="$reactionValue3" />
		</xsl:if>
		<xsl:if test="string-length($reactionValue4)>1">
			<xsl:if test="position()>1">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:value-of select="$reactionValue4" />
		</xsl:if>
		<xsl:if test="string-length($reactionValue5)>1">
			<xsl:if test="position()>1">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:value-of select="$reactionValue5" />
		</xsl:if>
		<xsl:if test="string-length($reactionValue6)>1">
			<xsl:if test="position()>1">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:value-of select="$reactionValue6" />
		</xsl:if>
	</xsl:template>

	<xsl:template name="getAllergySource">
		<xsl:param name="row" />
		<xsl:param name="isPlaintext" select="boolean(true)" />
		<xsl:choose>
			<xsl:when
				test="$row/n1:act/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name">
				<xsl:value-of
					select="$row/n1:act/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="getSeverity">
		<xsl:param name="observation" />
		<xsl:variable name="severityReference"
			select="$observation/n1:entryRelationship[@typeCode='SUBJ']/n1:observation/n1:text/n1:reference/@value" />

		<xsl:value-of select="../n1:text/n1:content[@ID=$severityReference]" />
		<xsl:value-of
			select="../n1:text/n1:content[@ID=substring-after($severityReference,'#')]" />

		<xsl:text> </xsl:text>


	</xsl:template>


	<xsl:template name="getReactionCoded">
		<xsl:param name="eR" />
		<xsl:variable name="coded"
			select="$eR[@inversionInd='true' and @typeCode='MFST']" />
		<xsl:for-each select="$coded">
			<xsl:if test="n1:observation/n1:value/n1:translation/@displayName">
				<xsl:value-of select="n1:observation/n1:value/n1:translation/@displayName" />
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="getProblemOnsetDateString">
		<xsl:param name="act" />
		<xsl:if test="$act">
			<xsl:choose>
				<xsl:when
					test="string-length($act/n1:entryRelationship/n1:observation/n1:effectiveTime/n1:low/@value)>1">
					<xsl:value-of
						select="$act/n1:entryRelationship/n1:observation/n1:effectiveTime/n1:low/@value" />
				</xsl:when>
				<xsl:when test="string-length($act/n1:effectiveTime/n1:low/@value)>1">
					<xsl:value-of select="n1:act/n1:effectiveTime/n1:low/@value" />
				</xsl:when>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getProblemCode">
		<xsl:param name="rowData" />
		<!--<xsl:variable name="probcodenull" select="$rowData/n1:code/@nullFlavor"/> -->
		<xsl:choose>
			<!--<xsl:when test="string($probcodenull)='UNK' "> <xsl:call-template 
				name="na"/> </xsl:when> -->
			<xsl:when test="$rowData/n1:value/@code and not($isKaiser)">
				<xsl:value-of select="$rowData/n1:value/@code" />
			</xsl:when>
			<xsl:when test="$rowData/n1:value/n1:translation/@code">
				<xsl:value-of select="$rowData/n1:value/n1:translation/@code" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="getEncounterID">
		<xsl:param name="encounter" />
		<xsl:choose>
			<xsl:when test="$encounter/n1:id/@root">
				<xsl:value-of select="$encounter/n1:id/@root" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="getEncounterType">
		<xsl:param name="encounter" />
		<xsl:choose>

			<xsl:when test="$encounter/n1:code/@displayName">
				<xsl:value-of select="$encounter/n1:code/@displayName" />
			</xsl:when>
			<xsl:when test="$encounter/n1:code/@code">
				<xsl:value-of select="$encounter/n1:code/@code" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>

	<xsl:template name="getEncounterFreeText">
		<xsl:param name="encounter" />
		<xsl:choose>
			<xsl:when
				test="starts-with($encounter/n1:code/n1:originalText/n1:reference/@value,'#')">
				<xsl:value-of
					select="$encounter/../../n1:text/n1:content[@ID=substring($encounter/n1:code/n1:originalText/n1:reference/@value,2)]" />
			</xsl:when>
			<xsl:when test="starts-with($encounter/n1:text/n1:reference/@value,'#')">
				<xsl:if
					test="$encounter/../../n1:text/n1:content[@ID=substring($encounter/n1:text/n1:reference/@value,2)]">
					<xsl:value-of
						select="$encounter/../../n1:text/n1:content[@ID=substring($encounter/n1:text/n1:reference/@value,2)]" />
				</xsl:if>
				<xsl:if
					test="$encounter/../../n1:text/n1:table/n1:tbody/n1:tr/n1:td[@ID=substring($encounter/n1:text/n1:reference/@value,2)]">
					<xsl:value-of
						select="$encounter/../../n1:text/n1:table/n1:tbody/n1:tr/n1:td[@ID=substring($encounter/n1:text/n1:reference/@value,2)]" />
				</xsl:if>

			</xsl:when>
			<xsl:when test="$encounter/n1:code/n1:originalText/n1:reference/@value">
				<xsl:value-of
					select="$encounter/../../n1:text/n1:content[@ID=$encounter/n1:code/n1:originalText/n1:reference/@value]" />
			</xsl:when>

			<xsl:when test="starts-with($encounter/n1:text/n1:reference/@value,'#')">
				<xsl:variable name="encRef"
					select="substring($encounter/n1:text/n1:reference/@value,2)" />
				<xsl:variable name="enc"
					select="$encounter/../../n1:text/n1:content[@ID=$encRef]" />
				<xsl:value-of select="$enc" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="encRef"
					select="$encounter/n1:code/n1:originalText/n1:reference/@value" />
				<xsl:variable name="enc"
					select="$encounter/../../n1:text/n1:content[@ID=$encRef]" />
				<xsl:value-of select="$enc" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>


	<xsl:template name="getEncounterDateTime">
		<xsl:param name="encounter" />
		<xsl:if test="$encounter">
			<xsl:choose>
				<xsl:when test="$encounter/n1:effectiveTime/n1:low/@value">
					<xsl:value-of select="$encounter/n1:effectiveTime/n1:low/@value" />
				</xsl:when>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getEncounterProvider">
		<xsl:param name="encounter" />
		<xsl:if test="$encounter">
			<xsl:choose>
				<xsl:when
					test="$encounter/n1:performer/n1:assignedEntity/n1:assignedPerson/n1:name/n1:family and $encounter/n1:performer/n1:assignedEntity/n1:assignedPerson/n1:name/n1:given">
					<xsl:variable name="first"
						select="$encounter/n1:performer/n1:assignedEntity/n1:assignedPerson/n1:name/n1:given" />
					<xsl:variable name="last"
						select="$encounter/n1:performer/n1:assignedEntity/n1:assignedPerson/n1:name/n1:family" />
					<xsl:call-template name="formatProviderName">
						<xsl:with-param name="row" select="." />
						<xsl:with-param name="first" select="$first" />
						<xsl:with-param name="last" select="$last" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of
						select="$encounter/n1:performer/n1:assignedEntity/n1:assignedPerson" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getEncounterSource">
		<xsl:param name="encounter" />
		<xsl:param name="isPlaintext" select="boolean(true)" />
		<xsl:choose>
			<xsl:when
				test="$encounter/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name">
				<xsl:value-of
					select="$encounter/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="getEncounterReason">
		<xsl:param name="encounter" />
		<xsl:param name="isPlaintext" select="boolean(true)" />
		<xsl:choose>
			<xsl:when
				test="$encounter/n1:entryRelationship/n1:observation/n1:text/n1:reference/@value">
				<xsl:variable name="encReasonRef"
					select="$encounter/n1:entryRelationship/n1:observation/n1:text/n1:reference/@value" />
				<xsl:choose>
					<xsl:when test="$encounter/../../n1:text/n1:content[@ID=$encReasonRef]">
						<xsl:value-of
							select="$encounter/../../n1:text/n1:content[@ID=$encReasonRef]" />
					</xsl:when>
					<xsl:when
						test="$encounter/../../n1:text/n1:content[@ID=substring($encReasonRef,2)]">
						<xsl:value-of
							select="$encounter/../../n1:text/n1:content[@ID=substring($encReasonRef,2)]" />
					</xsl:when>
				</xsl:choose>



			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>


	<xsl:template name="getProcedureID">
		<xsl:param name="procedure" />
		<xsl:if test="$procedure">
			<xsl:choose>
				<xsl:when test="$procedure/n1:id/@root">
					<xsl:value-of select="$procedure/n1:id/@root" />
				</xsl:when>
				<xsl:otherwise />
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getProcedureType">
		<xsl:param name="procedure" />
		<xsl:variable name="displayName" select="$procedure/n1:code/@displayName" />
		<xsl:variable name="code" select="$procedure/n1:code/@code" />
		<xsl:choose>
			<xsl:when test="contains($displayName,$code)">
				<xsl:value-of select="substring-after($displayName,$code)" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$procedure/n1:code/@displayName" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="getProcedureQualifier">
		<xsl:param name="procedure" />
		<xsl:value-of select="$procedure/n1:name/@displayName" />
		<xsl:text> </xsl:text>
		<xsl:value-of select="$procedure/n1:value/@displayName" />
	</xsl:template>


	<xsl:template name="getProcedureFreeText">
		<xsl:param name="procedure" />
		<xsl:choose>
			<xsl:when test="starts-with($procedure/n1:text/n1:reference/@value,'#')">
				<xsl:variable name="procRef"
					select="substring($procedure/n1:text/n1:reference/@value,2)" />
				<xsl:variable name="proc"
					select="$procedure/../../n1:text/n1:content[@ID=$procRef]" />
				<xsl:value-of select="$proc" />
			</xsl:when>
			<xsl:when
				test="starts-with($procedure/n1:code/n1:originalText/n1:reference/@value,'#')">
				<xsl:variable name="procRef"
					select="substring($procedure/n1:code/n1:originalText/n1:reference/@value,2)" />
				<xsl:variable name="proc"
					select="$procedure/../../n1:text/n1:content[@ID=$procRef]" />
				<xsl:value-of select="$proc" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="procRef"
					select="$procedure/n1:code/n1:originalText/n1:reference/@value" />
				<xsl:variable name="proc"
					select="$procedure/../../n1:text/n1:content[@ID=$procRef]" />
				<xsl:value-of select="$proc" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>


	<xsl:template name="getProcedureDateTime">
		<xsl:param name="procedure" />
		<xsl:if test="$procedure">
			<xsl:choose>
				<xsl:when test="$procedure/n1:effectiveTime/n1:low/@value">
					<xsl:value-of select="$procedure/n1:effectiveTime/n1:low/@value" />
				</xsl:when>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getProcedureProvider">
		<xsl:param name="procedure" />
		<xsl:if test="$procedure">
			<xsl:choose>
				<xsl:when
					test="$procedure/n1:performer/n1:assignedEntity/n1:assignedPerson/n1:name/n1:family and $procedure/n1:performer/n1:assignedEntity/n1:assignedPerson/n1:name/n1:given">
					<xsl:variable name="first"
						select="$procedure/n1:performer/n1:assignedEntity/n1:assignedPerson/n1:name/n1:given" />
					<xsl:variable name="last"
						select="$procedure/n1:performer/n1:assignedEntity/n1:assignedPerson/n1:name/n1:family" />
					<xsl:call-template name="formatProviderName">
						<xsl:with-param name="row" select="." />
						<xsl:with-param name="first" select="$first" />
						<xsl:with-param name="last" select="$last" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of
						select="$procedure/n1:performer/n1:assignedEntity/n1:assignedPerson" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>







	<xsl:template name="getProcedureSource">
		<xsl:param name="procedure" />
		<xsl:param name="isPlaintext" select="boolean(true)" />
		<xsl:choose>
			<xsl:when
				test="$procedure/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name">
				<xsl:value-of
					select="$procedure/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>





	<xsl:template name="medDateBeginString">
		<xsl:param name="substanceAdmin" />
		<xsl:if test="$substanceAdmin">
			<xsl:choose>

				<xsl:when
					test="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:entryRelationship[@typeCode='REFR']/n1:supply[@moodCode='EVN' and @classCode='SPLY']/n1:time/@value">
					<xsl:value-of
						select="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:entryRelationship[@typeCode='REFR']/n1:supply[@moodCode='EVN' and @classCode='SPLY']/n1:time/@value" />
				</xsl:when>
				<xsl:when
					test="$substanceAdmin/n1:entryRelationship[@typeCode='REFR']/n1:supply[@classCode='SPLY' and @moodCode='EVN']/n1:effectiveTime/@value">
					<xsl:value-of
						select="$substanceAdmin/n1:entryRelationship[@typeCode='REFR']/n1:supply[@classCode='SPLY' and @moodCode='EVN']/n1:effectiveTime/@value" />
				</xsl:when>

			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getMedicationName">
		<xsl:param name="substanceAdmin" />
		<xsl:param name="row" />
		<xsl:variable name="ref1"
			select="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code/n1:originalText/n1:reference/@value" />
		<xsl:if test="$substanceAdmin">
			<xsl:choose>
				<xsl:when test="$row/../n1:text/n1:list/n1:item/n1:content[@ID=$ref1]">
					<xsl:value-of
						select="$row/../n1:text/n1:list/n1:item/n1:content[@ID=$ref1]" />
				</xsl:when>
				<xsl:when
					test="$row/../n1:text/n1:list/n1:item/n1:content[@ID=substring-after($ref1,'#')]">
					<xsl:value-of
						select="$row/../n1:text/n1:list/n1:item/n1:content[@ID=substring-after($ref1,'#')]" />
				</xsl:when>
				<xsl:when
					test="$row/../n1:text/n1:content[@ID=substring-after($ref1,'#')]">
					<xsl:value-of
						select="$row/../n1:text/n1:content[@ID=substring-after($ref1,'#')]" />
				</xsl:when>
				<xsl:when
					test="$row/../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=substring-after($ref1,'#')]">
					<xsl:value-of
						select="$row/../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=substring-after($ref1,'#')]" />
				</xsl:when>
				<xsl:when
					test="$row/../n1:text/n1:table/n1:tbody/n1:tr/n1:td[@ID=substring-after($ref1,'#')]">
					<xsl:value-of
						select="$row/../n1:text/n1:table/n1:tbody/n1:tr/n1:td[@ID=substring-after($ref1,'#')]" />
				</xsl:when>
				<xsl:when
					test="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code/n1:originalText">
					<xsl:value-of
						select="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code/n1:originalText" />
				</xsl:when>
				<xsl:when
					test="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code/@displayName">
					<xsl:value-of
						select="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:code/@displayName" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of
						select="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:name" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="medExpireDateString">
		<xsl:param name="substanceAdmin" />
		<xsl:if test="$substanceAdmin">
			<xsl:choose>


				<xsl:when
					test="string-length($substanceAdmin/n1:entryRelationship[@typeCode='REFR']/n1:supply[@classCode='SPLY' and @moodCode='INT']/n1:effectiveTime/@value)>1">
					<xsl:value-of
						select="$substanceAdmin/n1:entryRelationship[@typeCode='REFR']/n1:supply[@classCode='SPLY' and @moodCode='INT']/n1:effectiveTime/@value" />
				</xsl:when>
				<xsl:when
					test="string-length($substanceAdmin/n1:entryRelationship[@typeCode='REFR']/n1:supply[@classCode='SPLY' and @moodCode='INT']/n1:effectiveTime/n1:high/@value)>1">
					<xsl:value-of
						select="$substanceAdmin/n1:entryRelationship[@typeCode='REFR']/n1:supply[@classCode='SPLY' and @moodCode='INT']/n1:effectiveTime/n1:high/@value" />
				</xsl:when>

			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getMedStatusString">
		<xsl:param name="substanceAdmin" />
		<xsl:if test="$substanceAdmin">
			<xsl:choose>
				<xsl:when
					test="string-length($substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:entryRelationship[@typeCode='REFR']/n1:observation[@classCode='OBS' and @moodCode='EVN' and n1:statusCode/@code='completed']/n1:value/@displayName)>1">
					<xsl:value-of
						select="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:entryRelationship[@typeCode='REFR']/n1:observation[@classCode='OBS' and @moodCode='EVN' and n1:statusCode/@code='completed']/n1:value/@displayName" />
				</xsl:when>

				<xsl:when
					test="string-length($substanceAdmin/n1:entryRelationship[@typeCode='REFR']/n1:observation[@classCode='OBS' and @moodCode='EVN']/n1:value/@displayName)>1">
					<xsl:value-of
						select="$substanceAdmin/n1:entryRelationship[@typeCode='REFR']/n1:observation[@classCode='OBS' and @moodCode='EVN']/n1:value/@displayName" />
				</xsl:when>

				<xsl:when
					test="string-length($substanceAdmin/n1:entryRelationship[@typeCode='REFR']/n1:observation[@classCode='OBS' and @moodCode='EVN']/n1:value/n1:originalText)>1">
					<xsl:value-of
						select="$substanceAdmin/n1:entryRelationship[@typeCode='REFR']/n1:observation[@classCode='OBS' and @moodCode='EVN']/n1:value/n1:originalText" />
				</xsl:when>




				<xsl:when
					test="string-length($substanceAdmin/n1:entryRelationship/n1:observation/n1:value[@xsi:type='CE']/n1:originalText)>1">
					<xsl:value-of
						select="$substanceAdmin/n1:entryRelationship/n1:observation/n1:value[@xsi:type='CE']/n1:originalText" />
				</xsl:when>
				<xsl:when
					test="string-length($substanceAdmin/n1:entryRelationship/n1:observation/n1:value[@xsi:type='CE']/@displayName)>1">
					<xsl:value-of
						select="$substanceAdmin/n1:entryRelationship/n1:observation/n1:value[@xsi:type='CE']/@displayName" />
				</xsl:when>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getRxNumString">
		<xsl:param name="substanceAdmin" />
		<xsl:if test="$substanceAdmin">
			<xsl:choose>
				<xsl:when
					test="string-length($substanceAdmin/n1:entryRelationship[@typeCode='REFR']/n1:supply[@classCode='SPLY' and @moodCode='EVN']/n1:id/@extension)>1">
					<xsl:value-of
						select="$substanceAdmin/n1:entryRelationship[@typeCode='REFR']/n1:supply[@classCode='SPLY' and @moodCode='EVN']/n1:id/@extension" />
				</xsl:when>
				<xsl:when
					test="string-length($substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:entryRelationship[@typeCode='REFR']/n1:supply[@moodCode='EVN' and @classCode='SPLY']/n1:id/@extension)>1">
					<xsl:value-of
						select="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:entryRelationship[@typeCode='REFR']/n1:supply[@moodCode='EVN' and @classCode='SPLY']/n1:id/@extension" />
				</xsl:when>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getMedProvider">
		<xsl:param name="substanceAdmin" />
		<xsl:variable name="assignedPerson1"
			select="$substanceAdmin/n1:entryRelationship[@typeCode='REFR']/n1:supply/n1:author/n1:assignedAuthor/n1:assignedPerson" />
		<xsl:variable name="assignedPerson2"
			select="$substanceAdmin/n1:entryRelationship/n1:supply/n1:author/n1:assignedAuthor/n1:assignedPerson" />
		<xsl:variable name="assignedPerson3"
			select="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:entryRelationship/n1:supply/n1:author/n1:assignedAuthor/n1:assignedPerson" />

		<xsl:if test="$substanceAdmin">

			<xsl:choose>

				<xsl:when test="string-length($assignedPerson1/n1:name)>1">
					<xsl:choose>
						<xsl:when
							test="$assignedPerson1/n1:name/n1:given and $assignedPerson1/n1:name/n1:family">
							<xsl:call-template name="formatProviderName">
								<xsl:with-param name="row" select="." />
								<xsl:with-param name="first"
									select="$assignedPerson1/n1:name/n1:given" />
								<xsl:with-param name="last"
									select="$assignedPerson1/n1:name/n1:family" />
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$assignedPerson1/n1:name" />
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>

				<xsl:when
					test="string-length($substanceAdmin/n1:entryRelationship/n1:supply/n1:author/n1:assignedAuthor/n1:assignedPerson/n1:name)>1">
					<xsl:choose>
						<xsl:when
							test="$assignedPerson2/n1:name/n1:given and $assignedPerson2/n1:name/n1:family">
							<xsl:call-template name="formatProviderName">
								<xsl:with-param name="row" select="." />
								<xsl:with-param name="first"
									select="$substanceAdmin/n1:entryRelationship/n1:supply/n1:author/n1:assignedAuthor/n1:assignedPerson/n1:name/n1:given" />
								<xsl:with-param name="last"
									select="$substanceAdmin/n1:entryRelationship/n1:supply/n1:author/n1:assignedAuthor/n1:assignedPerson/n1:name/n1:family" />
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of
								select="$substanceAdmin/n1:entryRelationship/n1:supply/n1:author/n1:assignedAuthor/n1:assignedPerson/n1:name" />
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>

				<xsl:when
					test="string-length($substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:entryRelationship/n1:supply/n1:author/n1:assignedAuthor/n1:assignedPerson/n1:name)>1">
					<xsl:choose>
						<xsl:when
							test="$assignedPerson3/n1:name/n1:given and $assignedPerson3/n1:name/n1:family">
							<xsl:call-template name="formatProviderName">
								<xsl:with-param name="row" select="." />
								<xsl:with-param name="first"
									select="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:entryRelationship/n1:supply/n1:author/n1:assignedAuthor/n1:assignedPerson/n1:name/n1:given" />
								<xsl:with-param name="last"
									select="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:entryRelationship/n1:supply/n1:author/n1:assignedAuthor/n1:assignedPerson/n1:name/n1:family" />
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of
								select="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:entryRelationship/n1:supply/n1:author/n1:assignedAuthor/n1:assignedPerson/n1:name" />
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>



				<!--<xsl:when test="string-length($substanceAdmin/n1:entryRelationship/n1:supply/n1:author/n1:assignedAuthor/n1:assignedPerson/n1:name)>1"> 
					<xsl:value-of select="$substanceAdmin/n1:entryRelationship/n1:supply/n1:author/n1:assignedAuthor/n1:assignedPerson/n1:name"/> 
					</xsl:when> <xsl:when test="string-length($substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:entryRelationship/n1:supply/n1:author/n1:assignedAuthor/n1:assignedPerson/n1:name)>1"> 
					<xsl:value-of select="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:entryRelationship/n1:supply/n1:author/n1:assignedAuthor/n1:assignedPerson/n1:name"/> 
					</xsl:when> -->

			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getMedQuantityString">
		<xsl:param name="substanceAdmin" />
		<xsl:if test="$substanceAdmin">
			<xsl:choose>
				<xsl:when
					test="string-length($substanceAdmin/n1:entryRelationship[@typeCode='REFR']/n1:supply/n1:quantity/@value)>1">
					<xsl:value-of
						select="$substanceAdmin/n1:entryRelationship[@typeCode='REFR']/n1:supply/n1:quantity/@value" />
				</xsl:when>
				<xsl:when
					test="string-length($substanceAdmin/n1:entryRelationship/n1:supply[@classCode='SPLY' and @moodCode='INT']/n1:quantity/@value)>0">
					<xsl:value-of
						select="$substanceAdmin/n1:entryRelationship/n1:supply[@classCode='SPLY' and @moodCode='INT']/n1:quantity/@value" />
				</xsl:when>
				<xsl:when
					test="string-length($substanceAdmin/n1:entryRelationship/n1:supply/n1:quantity/@value)>1">
					<xsl:value-of
						select="$substanceAdmin/n1:entryRelationship/n1:supply/n1:quantity/@value" />
				</xsl:when>
				<xsl:when test="string-length($substanceAdmin/n1:doseQuantity/@value)>1">
					<xsl:value-of select="$substanceAdmin/n1:doseQuantity/@value" />
				</xsl:when>
				<xsl:when
					test="string-length($substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:entryRelationship[@typeCode='REFR']/n1:supply[@classCode='SPLY' and @moodCode='INT']/n1:quantity/@value)>1">
					<xsl:value-of
						select="$substanceAdmin/n1:consumable/n1:manufacturedProduct/n1:manufacturedMaterial/n1:entryRelationship[@typeCode='REFR']/n1:supply[@classCode='SPLY' and @moodCode='INT']/n1:quantity/@value" />
				</xsl:when>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getSig">
		<xsl:param name="substanceAdmin" />
		<xsl:variable name="sigId1"
			select="$substanceAdmin/n1:text/n1:reference/@value" />
		<xsl:variable name="sigId2" select="substring-after($sigId1,'#')" />
		<!--<xsl:if test="$substanceAdmin/n1:text/n1:reference/@value"> -->
		<xsl:choose>
			<xsl:when
				test="$substanceAdmin/../../n1:text/n1:list/n1:item/n1:content[@ID=$sigId1]">
				<xsl:value-of
					select="$substanceAdmin/../../n1:text/n1:list/n1:item/n1:content[@ID=$sigId1]" />
			</xsl:when>
			<xsl:when
				test="$substanceAdmin/../../n1:text/n1:list/n1:item/n1:content[@ID=$sigId2]">
				<xsl:value-of
					select="$substanceAdmin/../../n1:text/n1:list/n1:item/n1:content[@ID=$sigId2]" />
			</xsl:when>
			<xsl:when
				test="$substanceAdmin/../../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=$sigId2]">
				<xsl:value-of
					select="$substanceAdmin/../../n1:text/n1:table/n1:tbody/n1:tr/n1:td/n1:content[@ID=$sigId2]" />
			</xsl:when>
			<xsl:otherwise />
		</xsl:choose>
		<!--</xsl:if> -->
	</xsl:template>

	<xsl:template name="getProblemSource">
		<xsl:param name="row" />
		<xsl:param name="isPlaintext" select="boolean(true)" />
		<xsl:choose>
			<xsl:when
				test="$row/n1:act/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name">
				<xsl:value-of
					select="$row/n1:act/n1:author/n1:assignedAuthor/n1:representedOrganization/n1:name" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="getProblemProvider">
		<xsl:param name="performer" />
		<xsl:variable name="providerRef"
			select="$performer/n1:assignedEntity/n1:id/@extension" />
		<xsl:choose>
			<xsl:when
				test="/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:performer/n1:assignedEntity/n1:id[@extension=$providerRef]/../n1:assignedPerson/n1:name">
				<xsl:value-of
					select="/n1:ClinicalDocument/n1:documentationOf/n1:serviceEvent/n1:performer/n1:assignedEntity/n1:id[@extension=$providerRef]/../n1:assignedPerson/n1:name" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="na" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- LABS -->

	<xsl:template name="getResultDT">
		<xsl:param name="observation" />
		<xsl:if test="$observation">
			<xsl:choose>
				<xsl:when test="$observation/n1:effectiveTime/@value">
					<xsl:call-template name="formatDate">
						<xsl:with-param name="date"
							select="$observation/n1:effectiveTime/@value" />
					</xsl:call-template>
				</xsl:when>
				<xsl:when
					test="n1:entry/n1:organizer/n1:component/n1:observation/n1:effectiveTime/@value">
					<xsl:call-template name="formatDate">
						<xsl:with-param name="date"
							select="n1:entry/n1:organizer/n1:component/n1:observation/n1:effectiveTime/@value" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise />
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getResultID">
		<xsl:param name="observation" />
		<xsl:if test="$observation">
			<xsl:choose>
				<xsl:when test="$observation/n1:id/@root">
					<xsl:value-of select="$observation/n1:id/@root" />
				</xsl:when>
				<xsl:otherwise />
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="getResultType">
		<xsl:param name="observation" />
		<xsl:choose>
			<!--<xsl:when test="$observation/n1:code/@code"> <xsl:value-of select="$observation/n1:code/@code"/> 
				</xsl:when> -->
			<xsl:when test="$observation/n1:code/@displayName">
				<xsl:value-of select="$observation/n1:code/@displayName" />
			</xsl:when>
			<xsl:otherwise />
		</xsl:choose>
	</xsl:template>

	<xsl:template name="getInterpretation">
		<xsl:param name="interpretation" />
		<xsl:choose>
			<xsl:when test="$interpretation/@displayName">
				<xsl:value-of select="$interpretation/@displayName" />
			</xsl:when>
			<xsl:when test="$interpretation/n1:originalText">
				<xsl:value-of select="$interpretation/n1:originalText" />
			</xsl:when>
			<xsl:otherwise />
		</xsl:choose>
	</xsl:template>

	<xsl:template name="getRefRange">
		<xsl:param name="row" />
		<xsl:choose>
			<xsl:when
				test="$row/n1:observation/n1:referenceRange/n1:observationRange/n1:text">
				<xsl:value-of
					select="$row/n1:observation/n1:referenceRange/n1:observationRange/n1:text" />
			</xsl:when>
			<xsl:when
				test="$row/n1:organizer/n1:component/n1:observation/n1:referenceRange/n1:observationRange/n1:text">
				<xsl:value-of
					select="$row/n1:organizer/n1:component/n1:observation/n1:referenceRange/n1:observationRange/n1:text" />
			</xsl:when>
			<xsl:otherwise />
		</xsl:choose>
	</xsl:template>


	<xsl:template name="na">
		<span title="Not Available">
			<xsl:text>--</xsl:text>
		</span>
	</xsl:template>

	<!-- Comments Section -->

	<!-- Med Comments -->
	<xsl:template name="medComments">
		<xsl:variable name="ref1"
			select="n1:entry/n1:act/n1:text/n1:reference/@value" />
		<xsl:variable name="ref2" select="substring-after($ref1,'#')" />
		<xsl:choose>
			<xsl:when test="n1:text/n1:content[@ID=$ref1]">
				<xsl:call-template name="formatComments">
					<xsl:with-param name="comments" select="n1:text/n1:content[@ID=$ref1]" />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="n1:text/n1:content[@ID=$ref2]">
				<xsl:call-template name="formatComments">
					<xsl:with-param name="comments" select="n1:text/n1:content[@ID=$ref2]" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Problem Comments -->
	<xsl:template name="probComments">
		<xsl:param name="section" />
		<xsl:variable name="ref1"
			select="n1:entry/n1:act/n1:text/n1:reference/@value" />
		<xsl:variable name="ref2" select="substring-after($ref1,'#')" />
		<xsl:choose>
			<xsl:when test="n1:text/n1:content[@ID=$ref1]">
				<xsl:call-template name="formatComments">
					<xsl:with-param name="comments" select="n1:text/n1:content[@ID=$ref1]" />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="n1:text/n1:content[@ID=$ref2]">
				<xsl:call-template name="formatComments">
					<xsl:with-param name="comments" select="n1:text/n1:content[@ID=$ref2]" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Allergy Comments -->
	<xsl:template name="allergyComments">
		<xsl:param name="section" />
		<xsl:variable name="ref1"
			select="n1:entry/n1:act/n1:text/n1:reference/@value" />
		<xsl:variable name="ref2" select="substring-after($ref1,'#')" />
		<xsl:choose>
			<xsl:when test="n1:text/n1:content[@ID=$ref1]">
				<xsl:call-template name="formatComments">
					<xsl:with-param name="comments" select="n1:text/n1:content[@ID=$ref1]" />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="n1:text/n1:content[@ID=$ref2]">
				<xsl:call-template name="formatComments">
					<xsl:with-param name="comments" select="n1:text/n1:content[@ID=$ref2]" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Results Comments -->
	<xsl:template name="resultsComments">
		<xsl:param name="section" />
		<xsl:variable name="ref1"
			select="n1:entry/n1:act/n1:text/n1:reference/@value" />
		<xsl:variable name="ref2" select="substring-after($ref1,'#')" />
		<xsl:choose>
			<xsl:when test="n1:text/n1:content[@ID=$ref1]">
				<xsl:call-template name="formatComments">
					<xsl:with-param name="comments" select="n1:text/n1:content[@ID=$ref1]" />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="n1:text/n1:content[@ID=$ref2]">
				<xsl:call-template name="formatComments">
					<xsl:with-param name="comments" select="n1:text/n1:content[@ID=$ref2]" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Encounters Comments -->
	<!-- Procedures Comments -->
	<!-- header elements -->
	<xsl:template name="documentGeneral">
		<table class="header_table">
			<tbody>
				<tr class="tr_header">
					<td width="20%" class="td_header_label">
						<span class="td_label">
							<xsl:text>Document Id</xsl:text>
						</span>
					</td>
					<td width="80%">
						<xsl:call-template name="show-id">
							<xsl:with-param name="id" select="n1:id" />
						</xsl:call-template>
					</td>
				</tr>
				<tr class="tr_header">
					<td width="20%" class="td_header_label">
						<span class="td_label">
							<xsl:text>Document Created:</xsl:text>
						</span>
					</td>
					<td width="80%">
						<xsl:call-template name="show-time">
							<xsl:with-param name="datetime" select="n1:effectiveTime" />
						</xsl:call-template>
					</td>
				</tr>
			</tbody>
		</table>
	</xsl:template>
	<!-- confidentiality -->
	<xsl:template name="confidentiality">
		<table class="header_table">
			<tbody>
				<tr class="tr_header">
					<td width="20%" class="td_header_label">
						<xsl:text>Confidentiality</xsl:text>
					</td>
					<td width="80%">
						<xsl:choose>
							<xsl:when test="n1:confidentialityCode/@code  = &apos;N&apos;">
								<xsl:text>Normal</xsl:text>
							</xsl:when>
							<xsl:when test="n1:confidentialityCode/@code  = &apos;R&apos;">
								<xsl:text>Restricted</xsl:text>
							</xsl:when>
							<xsl:when test="n1:confidentialityCode/@code  = &apos;V&apos;">
								<xsl:text>Very restricted</xsl:text>
							</xsl:when>
						</xsl:choose>
						<xsl:if test="n1:confidentialityCode/n1:originalText">
							<xsl:text> </xsl:text>
							<xsl:value-of select="n1:confidentialityCode/n1:originalText" />
						</xsl:if>
					</td>
				</tr>
			</tbody>
		</table>
	</xsl:template>
	<!-- author -->
	<xsl:template name="author">
		<xsl:if test="n1:author">
			<table class="header_table">
				<tbody>
					<xsl:for-each select="n1:author/n1:assignedAuthor">
						<tr class="tr_header">
							<td width="20%" class="td_header_label">
								<span class="td_label">
									<xsl:text>Author</xsl:text>
								</span>
							</td>
							<td width="80%">
								<xsl:choose>
									<xsl:when test="n1:assignedPerson/n1:name">
										<xsl:call-template name="show-name">
											<xsl:with-param name="name"
												select="n1:assignedPerson/n1:name" />
										</xsl:call-template>
										<xsl:if test="n1:representedOrganization">
											<xsl:text>, </xsl:text>
											<xsl:call-template name="show-name">
												<xsl:with-param name="name"
													select="n1:representedOrganization/n1:name" />
											</xsl:call-template>
										</xsl:if>
									</xsl:when>
									<xsl:when test="n1:assignedAuthoringDevice/n1:softwareName">
										<xsl:value-of select="n1:assignedAuthoringDevice/n1:softwareName" />
									</xsl:when>
									<xsl:when test="n1:representedOrganization">
										<xsl:call-template name="show-name">
											<xsl:with-param name="name"
												select="n1:representedOrganization/n1:name" />
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:for-each select="n1:id">
											<xsl:call-template name="show-id" />
											<br />
										</xsl:for-each>
									</xsl:otherwise>
								</xsl:choose>
							</td>
						</tr>
						<xsl:if test="n1:addr | n1:telecom">
							<tr class="tr_header">
								<td width="20%" class="td_header_label">
									<span class="td_label">
										<xsl:text>Contact info</xsl:text>
									</span>
								</td>
								<td width="80%">
									<xsl:call-template name="show-contactInfo">
										<xsl:with-param name="contact" select="." />
									</xsl:call-template>
								</td>
							</tr>
						</xsl:if>
					</xsl:for-each>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>
	<!-- authenticator -->
	<xsl:template name="authenticator">
		<xsl:if test="n1:authenticator">
			<table class="header_table">
				<tbody>
					<tr>
						<xsl:for-each select="n1:authenticator">
							<tr class="tr_header">
								<td width="20%" class="td_header_label">
									<span class="td_label">
										<xsl:text>Signed </xsl:text>
									</span>
								</td>
								<td width="80%">
									<xsl:call-template name="show-name">
										<xsl:with-param name="name"
											select="n1:assignedEntity/n1:assignedPerson/n1:name" />
									</xsl:call-template>
									<xsl:text> at </xsl:text>
									<xsl:call-template name="show-time">
										<xsl:with-param name="date" select="n1:time" />
									</xsl:call-template>
								</td>
							</tr>
							<xsl:if
								test="n1:assignedEntity/n1:addr | n1:assignedEntity/n1:telecom">
								<tr class="tr_header">
									<td width="20%" class="td_header_label">
										<span class="td_label">
											<xsl:text>Contact info</xsl:text>
										</span>
									</td>
									<td width="80%">
										<xsl:call-template name="show-contactInfo">
											<xsl:with-param name="contact" select="n1:assignedEntity" />
										</xsl:call-template>
									</td>
								</tr>
							</xsl:if>
						</xsl:for-each>
					</tr>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>
	<!-- legalAuthenticator -->
	<xsl:template name="legalAuthenticator">
		<xsl:if test="n1:legalAuthenticator">
			<table class="header_table">
				<tbody>
					<tr class="tr_header">
						<td width="20%" class="td_header_label">
							<span class="td_label">
								<xsl:text>Legal authenticator</xsl:text>
							</span>
						</td>
						<td width="80%">
							<xsl:call-template name="show-assignedEntity">
								<xsl:with-param name="asgnEntity"
									select="n1:legalAuthenticator/n1:assignedEntity" />
							</xsl:call-template>
							<xsl:text> </xsl:text>
							<xsl:call-template name="show-sig">
								<xsl:with-param name="sig"
									select="n1:legalAuthenticator/n1:signatureCode" />
							</xsl:call-template>
							<xsl:if test="n1:legalAuthenticator/n1:time/@value">
								<xsl:text> at </xsl:text>
								<xsl:call-template name="show-time">
									<xsl:with-param name="datetime"
										select="n1:legalAuthenticator/n1:time" />
								</xsl:call-template>
							</xsl:if>
						</td>
					</tr>
					<xsl:if
						test="n1:legalAuthenticator/n1:assignedEntity/n1:addr | n1:legalAuthenticator/n1:assignedEntity/n1:telecom">
						<tr class="tr_header">
							<td width="20%" class="td_header_label">
								<span class="td_label">
									<xsl:text>Contact info</xsl:text>
								</span>
							</td>
							<td width="80%">
								<xsl:call-template name="show-contactInfo">
									<xsl:with-param name="contact"
										select="n1:legalAuthenticator/n1:assignedEntity" />
								</xsl:call-template>
							</td>
						</tr>
					</xsl:if>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>
	<!-- dataEnterer -->
	<xsl:template name="dataEnterer">
		<xsl:if test="n1:dataEnterer">
			<table class="header_table">
				<tbody>
					<tr class="tr_header">
						<td width="20%" class="td_header_label">
							<span class="td_label">
								<xsl:text>Entered by</xsl:text>
							</span>
						</td>
						<td width="80%">
							<xsl:call-template name="show-assignedEntity">
								<xsl:with-param name="asgnEntity"
									select="n1:dataEnterer/n1:assignedEntity" />
							</xsl:call-template>
						</td>
					</tr>
					<xsl:if
						test="n1:dataEnterer/n1:assignedEntity/n1:addr | n1:dataEnterer/n1:assignedEntity/n1:telecom">
						<tr class="tr_header">
							<td width="20%" class="td_header_label">
								<span class="td_label">
									<xsl:text>Contact info</xsl:text>
								</span>
							</td>
							<td width="80%">
								<xsl:call-template name="show-contactInfo">
									<xsl:with-param name="contact"
										select="n1:dataEnterer/n1:assignedEntity" />
								</xsl:call-template>
							</td>
						</tr>
					</xsl:if>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>
	<!-- componentOf -->
	<xsl:template name="componentof">
		<xsl:if test="n1:componentOf">
			<table class="header_table">
				<tbody>
					<xsl:for-each select="n1:componentOf/n1:encompassingEncounter">
						<xsl:if test="n1:id">
							<tr class="tr_header">
								<xsl:choose>
									<xsl:when test="n1:code">
										<td width="20%" class="td_header_label">
											<span class="td_label">
												<xsl:text>Encounter Id</xsl:text>
											</span>
										</td>
										<td width="30%">
											<xsl:call-template name="show-id">
												<xsl:with-param name="id" select="n1:id" />
											</xsl:call-template>
										</td>
										<td width="20%" class="td_header_label">
											<span class="td_label">
												<xsl:text>Encounter Type</xsl:text>
											</span>
										</td>
										<td width="30%">
											<xsl:call-template name="show-code">
												<xsl:with-param name="code" select="n1:code" />
											</xsl:call-template>
										</td>
									</xsl:when>
									<xsl:otherwise>
										<td width="20%" class="td_header_label">
											<span class="td_label">
												<xsl:text>Encounter Id</xsl:text>
											</span>
										</td>
										<td width="80%">
											<xsl:call-template name="show-id">
												<xsl:with-param name="id" select="n1:id" />
											</xsl:call-template>
										</td>
									</xsl:otherwise>
								</xsl:choose>
							</tr>
						</xsl:if>
						<tr class="tr_header">
							<td width="20%" class="td_header_label">
								<span class="td_label">
									<xsl:text>Encounter Date</xsl:text>
								</span>
							</td>
							<td width="80%" colspan="3">
								<xsl:if test="n1:effectiveTime">
									<xsl:choose>
										<xsl:when test="n1:effectiveTime/@value">
											<xsl:text>&#160;at&#160;</xsl:text>
											<xsl:call-template name="show-time">
												<xsl:with-param name="datetime" select="n1:effectiveTime" />
											</xsl:call-template>
										</xsl:when>
										<xsl:when test="n1:effectiveTime/n1:low">
											<xsl:text>&#160;From&#160;</xsl:text>
											<xsl:call-template name="show-time">
												<xsl:with-param name="datetime"
													select="n1:effectiveTime/n1:low" />
											</xsl:call-template>
											<xsl:if test="n1:effectiveTime/n1:high">
												<xsl:text> to </xsl:text>
												<xsl:call-template name="show-time">
													<xsl:with-param name="datetime"
														select="n1:effectiveTime/n1:high" />
												</xsl:call-template>
											</xsl:if>
										</xsl:when>
									</xsl:choose>
								</xsl:if>
							</td>
						</tr>
						<xsl:if test="n1:location/n1:healthCareFacility">
							<tr class="tr_header">
								<td width="20%" class="td_header_label">
									<span class="td_label">
										<xsl:text>Encounter Location</xsl:text>
									</span>
								</td>
								<td width="80%" colspan="3">
									<xsl:choose>
										<xsl:when
											test="n1:location/n1:healthCareFacility/n1:location/n1:name">
											<xsl:call-template name="show-name">
												<xsl:with-param name="name"
													select="n1:location/n1:healthCareFacility/n1:location/n1:name" />
											</xsl:call-template>
											<xsl:for-each
												select="n1:location/n1:healthCareFacility/n1:serviceProviderOrganization/n1:name">
												<xsl:text> of </xsl:text>
												<xsl:call-template name="show-name">
													<xsl:with-param name="name"
														select="n1:location/n1:healthCareFacility/n1:serviceProviderOrganization/n1:name" />
												</xsl:call-template>
											</xsl:for-each>
										</xsl:when>
										<xsl:when test="n1:location/n1:healthCareFacility/n1:code">
											<xsl:call-template name="show-code">
												<xsl:with-param name="code"
													select="n1:location/n1:healthCareFacility/n1:code" />
											</xsl:call-template>
										</xsl:when>
										<xsl:otherwise>
											<xsl:if test="n1:location/n1:healthCareFacility/n1:id">
												<xsl:text>id: </xsl:text>
												<xsl:for-each select="n1:location/n1:healthCareFacility/n1:id">
													<xsl:call-template name="show-id">
														<xsl:with-param name="id" select="." />
													</xsl:call-template>
												</xsl:for-each>
											</xsl:if>
										</xsl:otherwise>
									</xsl:choose>
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="n1:responsibleParty">
							<tr class="tr_header">
								<td width="20%" class="td_header_label">
									<span class="td_label">
										<xsl:text>Responsible party</xsl:text>
									</span>
								</td>
								<td width="80%" colspan="3">
									<xsl:call-template name="show-assignedEntity">
										<xsl:with-param name="asgnEntity"
											select="n1:responsibleParty/n1:assignedEntity" />
									</xsl:call-template>
								</td>
							</tr>
						</xsl:if>
						<xsl:if
							test="n1:responsibleParty/n1:assignedEntity/n1:addr | n1:responsibleParty/n1:assignedEntity/n1:telecom">
							<tr class="tr_header">
								<td width="20%" class="td_header_label">
									<span class="td_label">
										<xsl:text>Contact info</xsl:text>
									</span>
								</td>
								<td width="80%" colspan="3">
									<xsl:call-template name="show-contactInfo">
										<xsl:with-param name="contact"
											select="n1:responsibleParty/n1:assignedEntity" />
									</xsl:call-template>
								</td>
							</tr>
						</xsl:if>
					</xsl:for-each>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>
	<!-- custodian -->
	<xsl:template name="custodian">
		<xsl:if test="n1:custodian">
			<table class="header_table">
				<tbody>
					<tr class="tr_header">
						<td width="20%" class="td_header_label">
							<span class="td_label">
								<xsl:text>Document maintained by</xsl:text>
							</span>
						</td>
						<td width="80%">
							<xsl:choose>
								<xsl:when
									test="n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:name">
									<xsl:call-template name="show-name">
										<xsl:with-param name="name"
											select="n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:name" />
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:for-each
										select="n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:id">
										<xsl:call-template name="show-id" />
										<xsl:if test="position()!=last()">
											<br />
										</xsl:if>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
					<xsl:if
						test="n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:addr |             n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization/n1:telecom">
						<tr class="tr_header">
							<td width="20%" class="td_header_label">
								<span class="td_label">
									<xsl:text>Contact info</xsl:text>
								</span>
							</td>
							<td width="80%">
								<xsl:call-template name="show-contactInfo">
									<xsl:with-param name="contact"
										select="n1:custodian/n1:assignedCustodian/n1:representedCustodianOrganization" />
								</xsl:call-template>
							</td>
						</tr>
					</xsl:if>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>
	<!-- documentationOf -->
	<xsl:template name="documentationOf">
		<xsl:if test="n1:documentationOf">
			<table class="header_table">
				<tbody>
					<xsl:for-each select="n1:documentationOf">
						<xsl:if test="n1:serviceEvent/@classCode and n1:serviceEvent/n1:code">
							<xsl:variable name="displayName">
								<xsl:call-template name="show-actClassCode">
									<xsl:with-param name="clsCode"
										select="n1:serviceEvent/@classCode" />
								</xsl:call-template>
							</xsl:variable>
							<xsl:if test="$displayName">
								<tr class="tr_header">
									<td width="20%" class="td_header_label">
										<span class="td_label">
											<xsl:call-template name="firstCharCaseUp">
												<xsl:with-param name="data" select="$displayName" />
											</xsl:call-template>
										</span>
									</td>
									<td width="80%" colspan="3">
										<xsl:call-template name="show-code">
											<xsl:with-param name="code" select="n1:serviceEvent/n1:code" />
										</xsl:call-template>
										<xsl:if test="n1:serviceEvent/n1:effectiveTime">
											<xsl:choose>
												<xsl:when test="n1:serviceEvent/n1:effectiveTime/@value">
													<xsl:text>&#160;at&#160;</xsl:text>
													<xsl:call-template name="show-time">
														<xsl:with-param name="datetime"
															select="n1:serviceEvent/n1:effectiveTime" />
													</xsl:call-template>
												</xsl:when>
												<xsl:when test="n1:serviceEvent/n1:effectiveTime/n1:low">
													<xsl:text>&#160;from&#160;</xsl:text>
													<xsl:call-template name="show-time">
														<xsl:with-param name="datetime"
															select="n1:serviceEvent/n1:effectiveTime/n1:low" />
													</xsl:call-template>
													<xsl:if test="n1:serviceEvent/n1:effectiveTime/n1:high">
														<xsl:text> to </xsl:text>
														<xsl:call-template name="show-time">
															<xsl:with-param name="datetime"
																select="n1:serviceEvent/n1:effectiveTime/n1:high" />
														</xsl:call-template>
													</xsl:if>
												</xsl:when>
											</xsl:choose>
										</xsl:if>
									</td>
								</tr>
							</xsl:if>
						</xsl:if>
						<xsl:for-each select="n1:serviceEvent/n1:performer">
							<xsl:variable name="displayName">
								<xsl:call-template name="show-participationType">
									<xsl:with-param name="ptype" select="@typeCode" />
								</xsl:call-template>
								<xsl:text> </xsl:text>
								<xsl:if test="n1:functionCode/@code">
									<xsl:call-template name="show-participationFunction">
										<xsl:with-param name="pFunction" select="n1:functionCode/@code" />
									</xsl:call-template>
								</xsl:if>
							</xsl:variable>
							<tr class="tr_header">
								<td width="20%" class="td_header_label">
									<span class="td_label">
										<xsl:call-template name="firstCharCaseUp">
											<xsl:with-param name="data" select="$displayName" />
										</xsl:call-template>
									</span>
								</td>
								<td width="80%" colspan="3">
									<xsl:call-template name="show-assignedEntity">
										<xsl:with-param name="asgnEntity" select="n1:assignedEntity" />
									</xsl:call-template>
								</td>
							</tr>
						</xsl:for-each>
					</xsl:for-each>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>
	<!-- inFulfillmentOf -->
	<xsl:template name="inFulfillmentOf">
		<xsl:if test="n1:infulfillmentOf">
			<table class="header_table">
				<tbody>
					<xsl:for-each select="n1:inFulfillmentOf">
						<tr class="tr_header">
							<td width="20%" class="td_header_label">
								<span class="td_label">
									<xsl:text>In fulfillment of</xsl:text>
								</span>
							</td>
							<td width="80%">
								<xsl:for-each select="n1:order">
									<xsl:for-each select="n1:id">
										<xsl:call-template name="show-id" />
									</xsl:for-each>
									<xsl:for-each select="n1:code">
										<xsl:text>&#160;</xsl:text>
										<xsl:call-template name="show-code">
											<xsl:with-param name="code" select="." />
										</xsl:call-template>
									</xsl:for-each>
									<xsl:for-each select="n1:priorityCode">
										<xsl:text>&#160;</xsl:text>
										<xsl:call-template name="show-code">
											<xsl:with-param name="code" select="." />
										</xsl:call-template>
									</xsl:for-each>
								</xsl:for-each>
							</td>
						</tr>
					</xsl:for-each>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>
	<!-- informant -->
	<xsl:template name="informant">
		<xsl:if test="n1:informant">
			<table class="header_table">
				<tbody>
					<xsl:for-each select="n1:informant">
						<tr class="tr_header">
							<td width="20%" class="td_header_label">
								<span class="td_label">
									<xsl:text>Informant</xsl:text>
								</span>
							</td>
							<td width="80%">
								<xsl:if test="n1:assignedEntity">
									<xsl:call-template name="show-assignedEntity">
										<xsl:with-param name="asgnEntity" select="n1:assignedEntity" />
									</xsl:call-template>
								</xsl:if>
								<xsl:if test="n1:relatedEntity">
									<xsl:call-template name="show-relatedEntity">
										<xsl:with-param name="relatedEntity" select="n1:relatedEntity" />
									</xsl:call-template>
								</xsl:if>
							</td>
						</tr>
						<xsl:choose>
							<xsl:when
								test="n1:assignedEntity/n1:addr | n1:assignedEntity/n1:telecom">
								<tr class="tr_header">
									<td width="20%" class="td_header_label">
										<span class="td_label">
											<xsl:text>Contact info</xsl:text>
										</span>
									</td>
									<td>
										<xsl:if test="n1:assignedEntity">
											<xsl:call-template name="show-contactInfo">
												<xsl:with-param name="contact" select="n1:assignedEntity" />
											</xsl:call-template>
										</xsl:if>
									</td>
								</tr>
							</xsl:when>
							<xsl:when
								test="n1:relatedEntity/n1:addr | n1:relatedEntity/n1:telecom">
								<tr class="tr_header">
									<td width="20%" class="td_header_label">
										<span class="td_label">
											<xsl:text>Contact info</xsl:text>
										</span>
									</td>
									<td width="80%">
										<xsl:if test="n1:relatedEntity">
											<xsl:call-template name="show-contactInfo">
												<xsl:with-param name="contact" select="n1:relatedEntity" />
											</xsl:call-template>
										</xsl:if>
									</td>
								</tr>
							</xsl:when>
						</xsl:choose>
					</xsl:for-each>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>
	<!-- informantionRecipient -->
	<xsl:template name="informationRecipient">
		<xsl:if test="n1:informationRecipient">
			<table class="header_table">
				<tbody>
					<xsl:for-each select="n1:informationRecipient">
						<tr class="tr_header">
							<td width="20%" class="td_header_label">
								<span class="td_label">
									<xsl:text>Information recipient:</xsl:text>
								</span>
							</td>
							<td width="80%">
								<xsl:choose>
									<xsl:when
										test="n1:intendedRecipient/n1:informationRecipient/n1:name">
										<xsl:for-each select="n1:intendedRecipient/n1:informationRecipient">
											<xsl:call-template name="show-name">
												<xsl:with-param name="name" select="n1:name" />
											</xsl:call-template>
											<xsl:if test="position() != last()">
												<br />
											</xsl:if>
										</xsl:for-each>
									</xsl:when>
									<xsl:otherwise>
										<xsl:for-each select="n1:intendedRecipient">
											<xsl:for-each select="n1:id">
												<xsl:call-template name="show-id" />
											</xsl:for-each>
											<xsl:if test="position() != last()">
												<br />
											</xsl:if>
											<br />
										</xsl:for-each>
									</xsl:otherwise>
								</xsl:choose>
							</td>
						</tr>
						<xsl:if
							test="n1:intendedRecipient/n1:addr | n1:intendedRecipient/n1:telecom">
							<tr class="tr_header">
								<td width="20%" class="td_header_label">
									<span class="td_label">
										<xsl:text>Contact info</xsl:text>
									</span>
								</td>
								<td>
									<xsl:call-template name="show-contactInfo">
										<xsl:with-param name="contact" select="n1:intendedRecipient" />
									</xsl:call-template>
								</td>
							</tr>
						</xsl:if>
					</xsl:for-each>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>
	<!-- participant -->
	<xsl:template name="participant">
		<xsl:if test="n1:participant">
			<table class="header_table">
				<tbody>
					<xsl:for-each select="n1:participant">
						<tr class="tr_header">
							<td width="20%" class="td_header_label">
								<xsl:variable name="participtRole">
									<xsl:call-template name="translateRoleAssoCode">
										<xsl:with-param name="classCode"
											select="n1:associatedEntity/@classCode" />
										<xsl:with-param name="code"
											select="n1:associatedEntity/n1:code" />
									</xsl:call-template>
								</xsl:variable>
								<xsl:choose>
									<xsl:when test="$participtRole">
										<span class="td_label">
											<xsl:call-template name="firstCharCaseUp">
												<xsl:with-param name="data" select="$participtRole" />
											</xsl:call-template>
										</span>
									</xsl:when>
									<xsl:otherwise>
										<span class="td_label">
											<xsl:text>Participant</xsl:text>
										</span>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="80%">
								<xsl:if test="n1:functionCode">
									<xsl:call-template name="show-code">
										<xsl:with-param name="code" select="n1:functionCode" />
									</xsl:call-template>
								</xsl:if>
								<xsl:call-template name="show-associatedEntity">
									<xsl:with-param name="assoEntity" select="n1:associatedEntity" />
								</xsl:call-template>
								<xsl:if test="n1:time">
									<xsl:if test="n1:time/n1:low">
										<xsl:text> from </xsl:text>
										<xsl:call-template name="show-time">
											<xsl:with-param name="datetime" select="n1:time/n1:low" />
										</xsl:call-template>
									</xsl:if>
									<xsl:if test="n1:time/n1:high">
										<xsl:text> to </xsl:text>
										<xsl:call-template name="show-time">
											<xsl:with-param name="datetime" select="n1:time/n1:high" />
										</xsl:call-template>
									</xsl:if>
								</xsl:if>
								<xsl:if test="position() != last()">
									<br />
								</xsl:if>
							</td>
						</tr>
						<xsl:if
							test="n1:associatedEntity/n1:addr | n1:associatedEntity/n1:telecom">
							<tr class="tr_header">
								<td width="20%" class="td_header_label">
									<span class="td_label">
										<xsl:text>Contact info</xsl:text>
									</span>
								</td>
								<td width="80%">
									<xsl:call-template name="show-contactInfo">
										<xsl:with-param name="contact" select="n1:associatedEntity" />
									</xsl:call-template>
								</td>
							</tr>
						</xsl:if>
					</xsl:for-each>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>
	<!-- recordTarget -->
	<xsl:template name="recordTarget">
		<table class="header_table">
			<tbody>
				<xsl:for-each select="/n1:ClinicalDocument/n1:recordTarget/n1:patientRole">
					<xsl:if test="not(n1:id/@nullFlavor)">
						<tr class="tr_header">
							<td width="20%" class="td_header_label">
								<span class="td_label">
									<xsl:text>Patient</xsl:text>
								</span>
							</td>
							<td width="80%" colspan="3">
								<xsl:call-template name="show-name">
									<xsl:with-param name="name" select="n1:patient/n1:name" />
								</xsl:call-template>
							</td>
						</tr>
						<tr class="tr_header">
							<td width="20%" class="td_header_label">
								<span class="td_label">
									<xsl:text>Date of birth</xsl:text>
								</span>
							</td>
							<td width="30%">
								<xsl:call-template name="show-time">
									<xsl:with-param name="datetime" select="n1:patient/n1:birthTime" />
								</xsl:call-template>
							</td>
							<td width="20%" class="td_header_label">
								<span class="td_label">
									<xsl:text>Sex</xsl:text>
								</span>
							</td>
							<td width="30%">
								<xsl:for-each select="n1:patient/n1:administrativeGenderCode">
									<xsl:call-template name="show-gender" />
								</xsl:for-each>
							</td>
						</tr>
						<xsl:if test="n1:patient/n1:raceCode | (n1:patient/n1:ethnicGroupCode)">
							<tr class="tr_header">
								<td width="20%" class="td_header_label">
									<span class="td_label">
										<xsl:text>Race</xsl:text>
									</span>
								</td>
								<td width="30%">
									<xsl:choose>
										<xsl:when test="n1:patient/n1:raceCode">
											<xsl:for-each select="n1:patient/n1:raceCode">
												<xsl:call-template name="show-race-ethnicity" />
											</xsl:for-each>
										</xsl:when>
										<xsl:otherwise>
											<xsl:text>Information not available</xsl:text>
										</xsl:otherwise>
									</xsl:choose>
								</td>
								<td width="20%" class="td_header_label">
									<span class="td_label">
										<xsl:text>Ethnicity</xsl:text>
									</span>
								</td>
								<td width="30%">
									<xsl:choose>
										<xsl:when test="n1:patient/n1:ethnicGroupCode">
											<xsl:for-each select="n1:patient/n1:ethnicGroupCode">
												<xsl:call-template name="show-race-ethnicity" />
											</xsl:for-each>
										</xsl:when>
										<xsl:otherwise>
											<xsl:text>Information not available</xsl:text>
										</xsl:otherwise>
									</xsl:choose>
								</td>
							</tr>
						</xsl:if>
						<tr class="tr_header">
							<td width="20%" class="td_header_label">
								<span class="td_label">
									<xsl:text>Contact info</xsl:text>
								</span>
							</td>
							<td width="30%">
								<xsl:call-template name="show-contactInfo">
									<xsl:with-param name="contact" select="." />
								</xsl:call-template>
							</td>
							<td width="20%" class="td_header_label">
								<span class="td_label">
									<xsl:text>Patient IDs</xsl:text>
								</span>
							</td>
							<td width="30%">
								<xsl:for-each select="n1:id">
									<xsl:call-template name="show-id" />
									<br />

								</xsl:for-each>
							</td>
						</tr>
					</xsl:if>
				</xsl:for-each>
			</tbody>
		</table>
	</xsl:template>
	<!-- relatedDocument -->
	<xsl:template name="relatedDocument">
		<xsl:if test="n1:relatedDocument">
			<table class="header_table">
				<tbody>
					<xsl:for-each select="n1:relatedDocument">
						<tr class="tr_header">
							<td width="20%" class="td_header_label">
								<span class="td_label">
									<xsl:text>Related document</xsl:text>
								</span>
							</td>
							<td width="80%">
								<xsl:for-each select="n1:parentDocument">
									<xsl:for-each select="n1:id">
										<xsl:call-template name="show-id" />
										<br />
									</xsl:for-each>
								</xsl:for-each>
							</td>
						</tr>
					</xsl:for-each>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>
	<!-- authorization (consent) -->
	<xsl:template name="authorization">
		<xsl:if test="n1:authorization">
			<table class="header_table">
				<tbody>
					<xsl:for-each select="n1:authorization">
						<tr class="tr_header">
							<td width="20%" class="td_header_label">
								<span class="td_label">
									<xsl:text>Consent</xsl:text>
								</span>
							</td>
							<td width="80%">
								<xsl:choose>
									<xsl:when test="n1:consent/n1:code">
										<xsl:call-template name="show-code">
											<xsl:with-param name="code" select="n1:consent/n1:code" />
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="show-code">
											<xsl:with-param name="code"
												select="n1:consent/n1:statusCode" />
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
								<br />
							</td>
						</tr>
					</xsl:for-each>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>
	<!-- setAndVersion -->
	<xsl:template name="setAndVersion">
		<xsl:if test="n1:setId and n1:versionNumber">
			<table class="header_table">
				<tbody>
					<tr>
						<td width="20%">
							<xsl:text>SetId and Version</xsl:text>
						</td>
						<td colspan="3">
							<xsl:text>SetId: </xsl:text>
							<xsl:call-template name="show-id">
								<xsl:with-param name="id" select="n1:setId" />
							</xsl:call-template>
							<xsl:text>  Version: </xsl:text>
							<xsl:value-of select="n1:versionNumber/@value" />
						</td>
					</tr>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>
	<!-- show nonXMLBody -->
	<xsl:template match='n1:component/n1:nonXMLBody'>
		<xsl:choose>
			<!-- if there is a reference, use that in an IFRAME -->
			<xsl:when test='n1:text/n1:reference'>
				<IFRAME name='nonXMLBody' id='nonXMLBody' WIDTH='80%' HEIGHT='600'
					src='{n1:text/n1:reference/@value}' />
			</xsl:when>
			<!-- Hardcoded link for supporting attachments - Asha Amritraj : START -->
			<xsl:when test='n1:text/@representation="B64"'>
				<pre>
					<!-- TODO: Asha Amritraj - Hardcoded C62 for now. Make it generic in 
						next release -->
					<a
						href="ViewAttachment.do_sec?mediaType={n1:text/@mediaType}&amp;representation={n1:text/@representation}&amp;docType=C62">View attachment</a>
				</pre>
			</xsl:when>
			<xsl:when test='n1:text/@mediaType="text/plain"'>
				<pre>
					<xsl:value-of select='n1:text/text()' />
				</pre>
			</xsl:when>
			<!-- Hardcoded link for supporting attachments - Asha Amritraj : END -->
			<xsl:otherwise>
				<CENTER>Cannot display the text</CENTER>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- top level component/section: display title and text, and process any 
		nested component/sections -->
	<xsl:template name="section">
		<xsl:call-template name="section-title">
			<xsl:with-param name="title" select="n1:title" />
		</xsl:call-template>
		<xsl:call-template name="section-author" />
		<xsl:call-template name="section-text" />
		<xsl:for-each select="n1:component/n1:section">
			<xsl:call-template name="nestedSection">
				<xsl:with-param name="margin" select="2" />
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>
	<!-- top level section title -->
	<xsl:template name="section-title">
		<xsl:param name="title" />
		<xsl:choose>
			<xsl:when
				test="count(/n1:ClinicalDocument/n1:component/n1:structuredBody/n1:component[n1:section]) &gt; 1">
				<h3>
					<a name="{generate-id($title)}" href="#toc">
						<xsl:value-of select="$title" />
					</a>
				</h3>
			</xsl:when>
			<xsl:otherwise>
				<h3>
					<xsl:value-of select="$title" />
				</h3>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- section author -->
	<xsl:template name="section-author">
		<xsl:if test="count(n1:author)&gt;0">
			<div style="margin-left : 2em;">
				<b>
					<xsl:text>Section Author: </xsl:text>
				</b>
				<xsl:for-each select="n1:author/n1:assignedAuthor">
					<xsl:choose>
						<xsl:when test="n1:assignedPerson/n1:name">
							<xsl:call-template name="show-name">
								<xsl:with-param name="name" select="n1:assignedPerson/n1:name" />
							</xsl:call-template>
							<xsl:if test="n1:representedOrganization">
								<xsl:text>, </xsl:text>
								<xsl:call-template name="show-name">
									<xsl:with-param name="name"
										select="n1:representedOrganization/n1:name" />
								</xsl:call-template>
							</xsl:if>
						</xsl:when>
						<xsl:when test="n1:assignedAuthoringDevice/n1:softwareName">
							<xsl:value-of select="n1:assignedAuthoringDevice/n1:softwareName" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:for-each select="n1:id">
								<xsl:call-template name="show-id" />
								<br />
							</xsl:for-each>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
				<br />
			</div>
		</xsl:if>
	</xsl:template>
	<!-- top-level section Text -->
	<xsl:template name="section-text">
		<div>
			<xsl:apply-templates select="n1:text" />
		</div>
	</xsl:template>
	<!-- nested component/section -->
	<xsl:template name="nestedSection">
		<xsl:param name="margin" />
		<h4 style="margin-left : {$margin}em;">
			<xsl:value-of select="n1:title" />
		</h4>
		<div style="margin-left : {$margin}em;">
			<xsl:apply-templates select="n1:text" />
		</div>
		<xsl:for-each select="n1:component/n1:section">
			<xsl:call-template name="nestedSection">
				<xsl:with-param name="margin" select="2*$margin" />
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>
	<!-- paragraph -->
	<xsl:template match="n1:paragraph">
		<p>
			<xsl:apply-templates />
		</p>
	</xsl:template>
	<!-- pre format -->
	<xsl:template match="n1:pre">
		<pre>
			<xsl:apply-templates />
		</pre>
	</xsl:template>
	<!-- Content w/ deleted text is hidden -->
	<xsl:template match="n1:content[@revised='delete']" />
	<!-- content -->
	<xsl:template match="n1:content">
		<xsl:apply-templates />
	</xsl:template>
	<!-- line break -->
	<xsl:template match="n1:br">
		<xsl:element name='br'>
			<xsl:apply-templates />
		</xsl:element>
	</xsl:template>
	<!-- list -->
	<xsl:template match="n1:list">
		<xsl:if test="n1:caption">
			<p>
				<b>
					<xsl:apply-templates select="n1:caption" />
				</b>
			</p>
		</xsl:if>
		<ul>
			<xsl:for-each select="n1:item">
				<li>
					<xsl:apply-templates />
				</li>
			</xsl:for-each>
		</ul>
	</xsl:template>
	<xsl:template match="n1:list[@listType='ordered']">
		<xsl:if test="n1:caption">
			<span style="font-weight:bold; ">
				<xsl:apply-templates select="n1:caption" />
			</span>
		</xsl:if>
		<ol>
			<xsl:for-each select="n1:item">
				<li>
					<xsl:apply-templates />
				</li>
			</xsl:for-each>
		</ol>
	</xsl:template>
	<!-- caption -->
	<xsl:template match="n1:caption">
		<xsl:apply-templates />
		<xsl:text>: </xsl:text>
	</xsl:template>
	<!-- Tables -->
	<xsl:template
		match="n1:table/@*|n1:thead/@*|n1:tfoot/@*|n1:tbody/@*|n1:colgroup/@*|n1:col/@*|n1:tr/@*|n1:th/@*|n1:td/@*">
		<xsl:copy>
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
		</xsl:copy>
	</xsl:template>
	<xsl:template match="n1:table">
		<table class="narr_table">
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
		</table>
	</xsl:template>
	<xsl:template match="n1:thead">
		<thead>
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
		</thead>
	</xsl:template>
	<xsl:template match="n1:tfoot">
		<tfoot>
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
		</tfoot>
	</xsl:template>
	<xsl:template match="n1:tbody">
		<tbody>
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
		</tbody>
	</xsl:template>
	<xsl:template match="n1:colgroup">
		<colgroup>
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
		</colgroup>
	</xsl:template>
	<xsl:template match="n1:col">
		<col>
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
		</col>
	</xsl:template>
	<xsl:template match="n1:tr">
		<tr class="narr_tr">
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
		</tr>
	</xsl:template>
	<xsl:template match="n1:th">
		<th class="narr_th">
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
		</th>
	</xsl:template>
	<xsl:template match="n1:td">
		<td>
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
		</td>
	</xsl:template>
	<xsl:template match="n1:table/n1:caption">
		<span style="font-weight:bold; ">
			<xsl:apply-templates />
		</span>
	</xsl:template>

	<!-- Asha Amritraj: Attachment Links - START -->
	<xsl:template match="n1:observationMedia">
		<!-- Hardcoded Link - TODO: Replace to dynamic link -->
		<pre>
			<a
				href="ViewAttachment.do_sec?mediaType={n1:value/@mediaType}&amp;representation={n1:value/@representation}&amp;docType=PrivacyConsentDirective">View attachment</a>
		</pre>
	</xsl:template>
	<!-- Asha Amritraj: Attachment Links - END -->


	<!-- RenderMultiMedia this currently only handles GIF's and JPEG's. It could, 
		however, be extended by including other image MIME types in the predicate 
		and/or by generating <object> or <applet> tag with the correct params depending 
		on the media type @ID =$imageRef referencedObject -->
	<xsl:template match="n1:renderMultiMedia">
		<xsl:variable name="imageRef" select="@referencedObject" />
		<xsl:choose>
			<xsl:when test="//n1:regionOfInterest[@ID=$imageRef]">
				<!-- Here is where the Region of Interest image referencing goes -->
				<xsl:if
					test="//n1:regionOfInterest[@ID=$imageRef]//n1:observationMedia/n1:value[@mediaType='image/gif' or
 @mediaType='image/jpeg']">
					<br clear="all" />
					<xsl:element name="img">
						<xsl:attribute name="src"><xsl:value-of
							select="//n1:regionOfInterest[@ID=$imageRef]//n1:observationMedia/n1:value/n1:reference/@value" /></xsl:attribute>
					</xsl:element>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<!-- Here is where the direct MultiMedia image referencing goes -->
				<xsl:if
					test="//n1:observationMedia[@ID=$imageRef]/n1:value[@mediaType='image/gif' or @mediaType='image/jpeg']">
					<br clear="all" />
					<xsl:element name="img">
						<xsl:attribute name="src"><xsl:value-of
							select="//n1:observationMedia[@ID=$imageRef]/n1:value/n1:reference/@value" /></xsl:attribute>
					</xsl:element>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- Stylecode processing Supports Bold, Underline and Italics display -->
	<xsl:template match="//n1:*[@styleCode]">
		<xsl:if test="@styleCode='Bold'">
			<xsl:element name="b">
				<xsl:apply-templates />
			</xsl:element>
		</xsl:if>
		<xsl:if test="@styleCode='Italics'">
			<xsl:element name="i">
				<xsl:apply-templates />
			</xsl:element>
		</xsl:if>
		<xsl:if test="@styleCode='Underline'">
			<xsl:element name="u">
				<xsl:apply-templates />
			</xsl:element>
		</xsl:if>
		<xsl:if
			test="contains(@styleCode,'Bold') and contains(@styleCode,'Italics') and not (contains(@styleCode, 'Underline'))">
			<xsl:element name="b">
				<xsl:element name="i">
					<xsl:apply-templates />
				</xsl:element>
			</xsl:element>
		</xsl:if>
		<xsl:if
			test="contains(@styleCode,'Bold') and contains(@styleCode,'Underline') and not (contains(@styleCode, 'Italics'))">
			<xsl:element name="b">
				<xsl:element name="u">
					<xsl:apply-templates />
				</xsl:element>
			</xsl:element>
		</xsl:if>
		<xsl:if
			test="contains(@styleCode,'Italics') and contains(@styleCode,'Underline') and not (contains(@styleCode, 'Bold'))">
			<xsl:element name="i">
				<xsl:element name="u">
					<xsl:apply-templates />
				</xsl:element>
			</xsl:element>
		</xsl:if>
		<xsl:if
			test="contains(@styleCode,'Italics') and contains(@styleCode,'Underline') and contains(@styleCode, 'Bold')">
			<xsl:element name="b">
				<xsl:element name="i">
					<xsl:element name="u">
						<xsl:apply-templates />
					</xsl:element>
				</xsl:element>
			</xsl:element>
		</xsl:if>
		<xsl:if
			test="not (contains(@styleCode,'Italics') or contains(@styleCode,'Underline') or contains(@styleCode, 'Bold'))">
			<xsl:apply-templates />
		</xsl:if>
	</xsl:template>
	<!-- Superscript or Subscript -->
	<xsl:template match="n1:sup">
		<xsl:element name="sup">
			<xsl:apply-templates />
		</xsl:element>
	</xsl:template>
	<xsl:template match="n1:sub">
		<xsl:element name="sub">
			<xsl:apply-templates />
		</xsl:element>
	</xsl:template>
	<!-- show-signature -->
	<xsl:template name="show-sig">
		<xsl:param name="sig" />
		<xsl:choose>
			<xsl:when test="$sig/@code =&apos;S&apos;">
				<xsl:text>signed</xsl:text>
			</xsl:when>
			<xsl:when test="$sig/@code=&apos;I&apos;">
				<xsl:text>intended</xsl:text>
			</xsl:when>
			<xsl:when test="$sig/@code=&apos;X&apos;">
				<xsl:text>signature required</xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- show-id -->
	<xsl:template name="show-id">
		<xsl:param name="id" />
		<xsl:choose>
			<xsl:when test="not($id)">
				<xsl:if test="not(@nullFlavor)">
					<xsl:if test="@extension">
						<xsl:value-of select="@extension" />
					</xsl:if>
					<xsl:text>:</xsl:text>
					<xsl:value-of select="@root" />
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="not($id/@nullFlavor)">
					<xsl:if test="$id/@extension">
						<xsl:value-of select="$id/@extension" />
					</xsl:if>
					<xsl:text>:</xsl:text>
					<xsl:value-of select="$id/@root" />
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- show-name -->
	<xsl:template name="show-name">
		<xsl:param name="name" />
		<xsl:choose>
			<xsl:when test="$name/n1:family">
				<xsl:if test="$name/n1:prefix">
					<xsl:value-of select="$name/n1:prefix" />
					<xsl:text> </xsl:text>
				</xsl:if>
				<xsl:value-of select="$name/n1:given" />
				<xsl:text> </xsl:text>
				<xsl:value-of select="$name/n1:family" />
				<xsl:if test="$name/n1:suffix">
					<xsl:text>, </xsl:text>
					<xsl:value-of select="$name/n1:suffix" />
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$name" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- show-gender -->
	<xsl:template name="show-gender">
		<xsl:choose>
			<xsl:when test="@code   = &apos;M&apos;">
				<xsl:text>Male</xsl:text>
			</xsl:when>
			<xsl:when test="@code  = &apos;F&apos;">
				<xsl:text>Female</xsl:text>
			</xsl:when>
			<xsl:when test="@code  = &apos;U&apos;">
				<xsl:text>Undifferentiated</xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- show-race-ethnicity -->
	<xsl:template name="show-race-ethnicity">
		<xsl:choose>
			<xsl:when test="@displayName">
				<xsl:value-of select="@displayName" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="@code" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- show-contactInfo -->
	<xsl:template name="show-contactInfo">
		<xsl:param name="contact" />
		<xsl:call-template name="show-address">
			<xsl:with-param name="address" select="$contact/n1:addr" />
		</xsl:call-template>
		<xsl:call-template name="show-telecom">
			<xsl:with-param name="telecom" select="$contact/n1:telecom" />
		</xsl:call-template>
	</xsl:template>
	<!-- show-address -->
	<xsl:template name="show-address">
		<xsl:param name="address" />
		<xsl:choose>
			<xsl:when test="$address">
				<xsl:if test="$address/@use">
					<xsl:text> </xsl:text>
					<xsl:call-template name="translateTelecomCode">
						<xsl:with-param name="code" select="$address/@use" />
					</xsl:call-template>
					<xsl:text>:</xsl:text>
					<br />
				</xsl:if>
				<xsl:for-each select="$address/n1:streetAddressLine">
					<xsl:value-of select="." />
					<br />
				</xsl:for-each>
				<xsl:if test="$address/n1:streetName">
					<xsl:value-of select="$address/n1:streetName" />
					<xsl:text> </xsl:text>
					<xsl:value-of select="$address/n1:houseNumber" />
					<br />
				</xsl:if>
				<xsl:if test="string-length($address/n1:city)>0">
					<xsl:value-of select="$address/n1:city" />
				</xsl:if>
				<xsl:if test="string-length($address/n1:state)>0">
					<xsl:text>,&#160;</xsl:text>
					<xsl:value-of select="$address/n1:state" />
				</xsl:if>
				<xsl:if test="string-length($address/n1:postalCode)>0">
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$address/n1:postalCode" />
				</xsl:if>
				<xsl:if test="string-length($address/n1:country)>0">
					<xsl:text>,&#160;</xsl:text>
					<xsl:value-of select="$address/n1:country" />
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>address not available</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		<br />
	</xsl:template>
	<!-- show-telecom -->
	<xsl:template name="show-telecom">
		<xsl:param name="telecom" />
		<xsl:choose>
			<xsl:when test="$telecom">
				<xsl:variable name="type"
					select="substring-before($telecom/@value, ':')" />
				<xsl:variable name="value"
					select="substring-after($telecom/@value, ':')" />
				<xsl:if test="$type">
					<xsl:call-template name="translateTelecomCode">
						<xsl:with-param name="code" select="$type" />
					</xsl:call-template>
					<xsl:if test="@use">
						<xsl:text> (</xsl:text>
						<xsl:call-template name="translateTelecomCode">
							<xsl:with-param name="code" select="@use" />
						</xsl:call-template>
						<xsl:text>)</xsl:text>
					</xsl:if>
					<xsl:text>: </xsl:text>
					<xsl:text> </xsl:text>
					<xsl:value-of select="$value" />
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>Telecom information not available</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		<br />
	</xsl:template>
	<!-- show-recipientType -->
	<xsl:template name="show-recipientType">
		<xsl:param name="typeCode" />
		<xsl:choose>
			<xsl:when test="$typeCode='PRCP'">
				Primary Recipient:
			</xsl:when>
			<xsl:when test="$typeCode='TRC'">
				Secondary Recipient:
			</xsl:when>
			<xsl:otherwise>
				Recipient:
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- Convert Telecom URL to display text -->
	<xsl:template name="translateTelecomCode">
		<xsl:param name="code" />
		<!--xsl:value-of select="document('voc.xml')/systems/system[@root=$code/@codeSystem]/code[@value=$code/@code]/@displayName"/ -->
		<!--xsl:value-of select="document('codes.xml')/*/code[@code=$code]/@display"/ -->
		<xsl:choose>
			<!-- lookup table Telecom URI -->
			<xsl:when test="$code='tel'">
				<xsl:text>Tel</xsl:text>
			</xsl:when>
			<xsl:when test="$code='fax'">
				<xsl:text>Fax</xsl:text>
			</xsl:when>
			<xsl:when test="$code='http'">
				<xsl:text>Web</xsl:text>
			</xsl:when>
			<xsl:when test="$code='mailto'">
				<xsl:text>Mail</xsl:text>
			</xsl:when>
			<xsl:when test="$code='H'">
				<xsl:text>Home</xsl:text>
			</xsl:when>
			<xsl:when test="$code='HV'">
				<xsl:text>Vacation Home</xsl:text>
			</xsl:when>
			<xsl:when test="$code='HP'">
				<xsl:text>Pirmary Home</xsl:text>
			</xsl:when>
			<xsl:when test="$code='WP'">
				<xsl:text>Work Place</xsl:text>
			</xsl:when>
			<xsl:when test="$code='PUB'">
				<xsl:text>Pub</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>{$code='</xsl:text>
				<xsl:value-of select="$code" />
				<xsl:text>'?}</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- convert RoleClassAssociative code to display text -->
	<xsl:template name="translateRoleAssoCode">
		<xsl:param name="classCode" />
		<xsl:param name="code" />
		<xsl:choose>
			<xsl:when test="$classCode='AFFL'">
				<xsl:text>affiliate</xsl:text>
			</xsl:when>
			<xsl:when test="$classCode='AGNT'">
				<xsl:text>agent</xsl:text>
			</xsl:when>
			<xsl:when test="$classCode='ASSIGNED'">
				<xsl:text>assigned entity</xsl:text>
			</xsl:when>
			<xsl:when test="$classCode='COMPAR'">
				<xsl:text>commissioning party</xsl:text>
			</xsl:when>
			<xsl:when test="$classCode='CON'">
				<xsl:text>contact</xsl:text>
			</xsl:when>
			<xsl:when test="$classCode='ECON'">
				<xsl:text>emergency contact</xsl:text>
			</xsl:when>
			<xsl:when test="$classCode='NOK'">
				<xsl:text>next of kin</xsl:text>
			</xsl:when>
			<xsl:when test="$classCode='SGNOFF'">
				<xsl:text>signing authority</xsl:text>
			</xsl:when>
			<xsl:when test="$classCode='GUARD'">
				<xsl:text>guardian</xsl:text>
			</xsl:when>
			<xsl:when test="$classCode='GUAR'">
				<xsl:text>guardian</xsl:text>
			</xsl:when>
			<xsl:when test="$classCode='CIT'">
				<xsl:text>citizen</xsl:text>
			</xsl:when>
			<xsl:when test="$classCode='COVPTY'">
				<xsl:text>covered party</xsl:text>
			</xsl:when>
			<xsl:when test="$classCode='PRS'">
				<xsl:text>personal relationship</xsl:text>
			</xsl:when>
			<xsl:when test="$classCode='CAREGIVER'">
				<xsl:text>care giver</xsl:text>
			</xsl:when>
			<xsl:when test="$classCode='PROV'">
				<xsl:text>healthcare provider</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>{$classCode='</xsl:text>
				<xsl:value-of select="$classCode" />
				<xsl:text>'?}</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if
			test="($code/@code) and ($code/@codeSystem='2.16.840.1.113883.5.111')">
			<xsl:text> </xsl:text>
			<xsl:choose>
				<xsl:when test="$code/@code='FTH'">
					<xsl:text>(Father)</xsl:text>
				</xsl:when>
				<xsl:when test="$code/@code='MTH'">
					<xsl:text>(Mother)</xsl:text>
				</xsl:when>
				<xsl:when test="$code/@code='NPRN'">
					<xsl:text>(Natural parent)</xsl:text>
				</xsl:when>
				<xsl:when test="$code/@code='STPPRN'">
					<xsl:text>(Step parent)</xsl:text>
				</xsl:when>
				<xsl:when test="$code/@code='SONC'">
					<xsl:text>(Son)</xsl:text>
				</xsl:when>
				<xsl:when test="$code/@code='DAUC'">
					<xsl:text>(Daughter)</xsl:text>
				</xsl:when>
				<xsl:when test="$code/@code='CHILD'">
					<xsl:text>(Child)</xsl:text>
				</xsl:when>
				<xsl:when test="$code/@code='EXT'">
					<xsl:text>(Extended family member)</xsl:text>
				</xsl:when>
				<xsl:when test="$code/@code='NBOR'">
					<xsl:text>(Neighbor)</xsl:text>
				</xsl:when>
				<xsl:when test="$code/@code='SIGOTHR'">
					<xsl:text>(Significant other)</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>{$code/@code='</xsl:text>
					<xsl:value-of select="$code/@code" />
					<xsl:text>'?}</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	<!-- show time -->
	<xsl:template name="show-time">
		<xsl:param name="datetime" />
		<xsl:choose>
			<xsl:when test="not($datetime)">
				<xsl:call-template name="formatDateTime">
					<xsl:with-param name="date" select="@value" />
				</xsl:call-template>
				<xsl:text> </xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="formatDateTime">
					<xsl:with-param name="date" select="$datetime/@value" />
				</xsl:call-template>
				<xsl:text> </xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- paticipant facility and date -->
	<xsl:template name="facilityAndDates">
		<table class="header_table">
			<tbody>
				<!-- facility id -->
				<tr class="tr_header">
					<td width="20%" class="td_header_label">
						<span class="td_label">
							<xsl:text>Facility ID</xsl:text>
						</span>
					</td>
					<td colspan="3">
						<xsl:choose>
							<xsl:when
								test="count(/n1:ClinicalDocument/n1:participant
                                      [@typeCode='LOC'][@contextControlCode='OP']
                                      /n1:associatedEntity[@classCode='SDLOC']/n1:id)&gt;0">
								<!-- change context node -->
								<xsl:for-each
									select="/n1:ClinicalDocument/n1:participant
                                      [@typeCode='LOC'][@contextControlCode='OP']
                                      /n1:associatedEntity[@classCode='SDLOC']/n1:id">
									<xsl:call-template name="show-id" />
									<!-- change context node again, for the code -->
									<xsl:for-each select="../n1:code">
										<xsl:text> (</xsl:text>
										<xsl:call-template name="show-code">
											<xsl:with-param name="code" select="." />
										</xsl:call-template>
										<xsl:text>)</xsl:text>
									</xsl:for-each>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>
								Not available
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
				<!-- Period reported -->
				<tr class="tr_header">
					<td width="20%" class="td_header_label">
						<span class="td_label">
							<xsl:text>First day of period reported</xsl:text>
						</span>
					</td>
					<td width="80%" colspan="3">
						<xsl:call-template name="show-time">
							<xsl:with-param name="datetime"
								select="/n1:ClinicalDocument/n1:documentationOf
                                      /n1:serviceEvent/n1:effectiveTime/n1:low" />
						</xsl:call-template>
					</td>
				</tr>
				<tr class="tr_header">
					<td width="20%" class="td_header_label">
						<span class="td_label">
							<xsl:text>Last day of period reported</xsl:text>
						</span>
					</td>
					<td width="80%" colspan="3">
						<xsl:call-template name="show-time">
							<xsl:with-param name="datetime"
								select="/n1:ClinicalDocument/n1:documentationOf
                                      /n1:serviceEvent/n1:effectiveTime/n1:high" />
						</xsl:call-template>
					</td>
				</tr>
			</tbody>
		</table>
	</xsl:template>
	<!-- show assignedEntity -->
	<xsl:template name="show-assignedEntity">
		<xsl:param name="asgnEntity" />
		<xsl:choose>
			<xsl:when test="$asgnEntity/n1:assignedPerson/n1:name">
				<xsl:call-template name="show-name">
					<xsl:with-param name="name"
						select="$asgnEntity/n1:assignedPerson/n1:name" />
				</xsl:call-template>
				<xsl:if test="$asgnEntity/n1:representedOrganization/n1:name">
					<xsl:text> of </xsl:text>
					<xsl:value-of select="$asgnEntity/n1:representedOrganization/n1:name" />
				</xsl:if>
			</xsl:when>
			<xsl:when test="$asgnEntity/n1:representedOrganization">
				<xsl:value-of select="$asgnEntity/n1:representedOrganization/n1:name" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$asgnEntity/n1:id">
					<xsl:call-template name="show-id" />
					<xsl:choose>
						<xsl:when test="position()!=last()">
							<xsl:text>, </xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<br />
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- show relatedEntity -->
	<xsl:template name="show-relatedEntity">
		<xsl:param name="relatedEntity" />
		<xsl:choose>
			<xsl:when test="$relatedEntity/n1:relatedPerson/n1:name">
				<xsl:call-template name="show-name">
					<xsl:with-param name="name"
						select="$relatedEntity/n1:relatedPerson/n1:name" />
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- show associatedEntity -->
	<xsl:template name="show-associatedEntity">
		<xsl:param name="assoEntity" />
		<xsl:choose>
			<xsl:when test="$assoEntity/n1:associatedPerson">
				<xsl:for-each select="$assoEntity/n1:associatedPerson/n1:name">
					<xsl:call-template name="show-name">
						<xsl:with-param name="name" select="." />
					</xsl:call-template>
					<br />
				</xsl:for-each>
			</xsl:when>
			<xsl:when test="$assoEntity/n1:scopingOrganization">
				<xsl:for-each select="$assoEntity/n1:scopingOrganization">
					<xsl:if test="n1:name">
						<xsl:call-template name="show-name">
							<xsl:with-param name="name" select="n1:name" />
						</xsl:call-template>
						<br />
					</xsl:if>
					<xsl:if test="n1:standardIndustryClassCode">
						<xsl:value-of select="n1:standardIndustryClassCode/@displayName" />
						<xsl:text> code:</xsl:text>
						<xsl:value-of select="n1:standardIndustryClassCode/@code" />
					</xsl:if>
				</xsl:for-each>
			</xsl:when>
			<xsl:when test="$assoEntity/n1:code">
				<xsl:call-template name="show-code">
					<xsl:with-param name="code" select="$assoEntity/n1:code" />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$assoEntity/n1:id">
				<xsl:value-of select="$assoEntity/n1:id/@extension" />
				<xsl:text> </xsl:text>
				<xsl:value-of select="$assoEntity/n1:id/@root" />
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- show code if originalText present, return it, otherwise, check and 
		return attribute: display name -->
	<xsl:template name="show-code">
		<xsl:param name="code" />
		<xsl:variable name="this-codeSystem">
			<xsl:value-of select="$code/@codeSystem" />
		</xsl:variable>
		<xsl:variable name="this-code">
			<xsl:value-of select="$code/@code" />
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$code/n1:originalText">
				<xsl:value-of select="$code/n1:originalText" />
			</xsl:when>
			<xsl:when test="$code/@displayName">
				<xsl:value-of select="$code/@displayName" />
			</xsl:when>
			<!-- <xsl:when test="$the-valuesets/*/voc:system[@root=$this-codeSystem]/voc:code[@value=$this-code]/@displayName"> 
				<xsl:value-of select="$the-valuesets/*/voc:system[@root=$this-codeSystem]/voc:code[@value=$this-code]/@displayName"/> 
				</xsl:when> -->
			<xsl:otherwise>
				<xsl:value-of select="$this-code" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- show classCode -->
	<xsl:template name="show-actClassCode">
		<xsl:param name="clsCode" />
		<xsl:choose>
			<xsl:when test=" $clsCode = 'ACT' ">
				<xsl:text>healthcare service</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'ACCM' ">
				<xsl:text>accommodation</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'ACCT' ">
				<xsl:text>account</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'ACSN' ">
				<xsl:text>accession</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'ADJUD' ">
				<xsl:text>financial adjudication</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'CONS' ">
				<xsl:text>consent</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'CONTREG' ">
				<xsl:text>container registration</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'CTTEVENT' ">
				<xsl:text>clinical trial timepoint event</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'DISPACT' ">
				<xsl:text>disciplinary action</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'ENC' ">
				<xsl:text>encounter</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'INC' ">
				<xsl:text>incident</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'INFRM' ">
				<xsl:text>inform</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'INVE' ">
				<xsl:text>invoice element</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'LIST' ">
				<xsl:text>working list</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'MPROT' ">
				<xsl:text>monitoring program</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'PCPR' ">
				<xsl:text>care provision</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'PROC' ">
				<xsl:text>procedure</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'REG' ">
				<xsl:text>registration</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'REV' ">
				<xsl:text>review</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'SBADM' ">
				<xsl:text>substance administration</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'SPCTRT' ">
				<xsl:text>speciment treatment</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'SUBST' ">
				<xsl:text>substitution</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'TRNS' ">
				<xsl:text>transportation</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'VERIF' ">
				<xsl:text>verification</xsl:text>
			</xsl:when>
			<xsl:when test=" $clsCode = 'XACT' ">
				<xsl:text>financial transaction</xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- show participationType -->
	<xsl:template name="show-participationType">
		<xsl:param name="ptype" />
		<xsl:choose>
			<xsl:when test=" $ptype='PPRF' ">
				<xsl:text>primary performer</xsl:text>
			</xsl:when>
			<xsl:when test=" $ptype='PRF' ">
				<xsl:text>performer</xsl:text>
			</xsl:when>
			<xsl:when test=" $ptype='VRF' ">
				<xsl:text>verifier</xsl:text>
			</xsl:when>
			<xsl:when test=" $ptype='SPRF' ">
				<xsl:text>secondary performer</xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- show participationFunction -->
	<xsl:template name="show-participationFunction">
		<xsl:param name="pFunction" />
		<xsl:choose>
			<!-- From the HL7 v3 ParticipationFunction code system -->
			<xsl:when test=" $pFunction = 'ADMPHYS' ">
				<xsl:text>(admitting physician)</xsl:text>
			</xsl:when>
			<xsl:when test=" $pFunction = 'ANEST' ">
				<xsl:text>(anesthesist)</xsl:text>
			</xsl:when>
			<xsl:when test=" $pFunction = 'ANRS' ">
				<xsl:text>(anesthesia nurse)</xsl:text>
			</xsl:when>
			<xsl:when test=" $pFunction = 'ATTPHYS' ">
				<xsl:text>(attending physician)</xsl:text>
			</xsl:when>
			<xsl:when test=" $pFunction = 'DISPHYS' ">
				<xsl:text>(discharging physician)</xsl:text>
			</xsl:when>
			<xsl:when test=" $pFunction = 'FASST' ">
				<xsl:text>(first assistant surgeon)</xsl:text>
			</xsl:when>
			<xsl:when test=" $pFunction = 'MDWF' ">
				<xsl:text>(midwife)</xsl:text>
			</xsl:when>
			<xsl:when test=" $pFunction = 'NASST' ">
				<xsl:text>(nurse assistant)</xsl:text>
			</xsl:when>
			<xsl:when test=" $pFunction = 'PCP' ">
				<xsl:text>(primary care physician)</xsl:text>
			</xsl:when>
			<xsl:when test=" $pFunction = 'PRISURG' ">
				<xsl:text>(primary surgeon)</xsl:text>
			</xsl:when>
			<xsl:when test=" $pFunction = 'RNDPHYS' ">
				<xsl:text>(rounding physician)</xsl:text>
			</xsl:when>
			<xsl:when test=" $pFunction = 'SASST' ">
				<xsl:text>(second assistant surgeon)</xsl:text>
			</xsl:when>
			<xsl:when test=" $pFunction = 'SNRS' ">
				<xsl:text>(scrub nurse)</xsl:text>
			</xsl:when>
			<xsl:when test=" $pFunction = 'TASST' ">
				<xsl:text>(third assistant)</xsl:text>
			</xsl:when>
			<!-- From the HL7 v2 Provider Role code system (2.16.840.1.113883.12.443) 
				which is used by HITSP -->
			<xsl:when test=" $pFunction = 'CP' ">
				<xsl:text>(consulting provider)</xsl:text>
			</xsl:when>
			<xsl:when test=" $pFunction = 'PP' ">
				<xsl:text>(primary care provider)</xsl:text>
			</xsl:when>
			<xsl:when test=" $pFunction = 'RP' ">
				<xsl:text>(referring provider)</xsl:text>
			</xsl:when>
			<xsl:when test=" $pFunction = 'MP' ">
				<xsl:text>(medical home provider)</xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="formatDateTime">
		<xsl:param name="date" />
		<!-- month -->
		<xsl:variable name="month" select="substring ($date, 5, 2)" />
		<xsl:choose>
			<xsl:when test="$month='01'">
				<xsl:text>January </xsl:text>
			</xsl:when>
			<xsl:when test="$month='02'">
				<xsl:text>February </xsl:text>
			</xsl:when>
			<xsl:when test="$month='03'">
				<xsl:text>March </xsl:text>
			</xsl:when>
			<xsl:when test="$month='04'">
				<xsl:text>April </xsl:text>
			</xsl:when>
			<xsl:when test="$month='05'">
				<xsl:text>May </xsl:text>
			</xsl:when>
			<xsl:when test="$month='06'">
				<xsl:text>June </xsl:text>
			</xsl:when>
			<xsl:when test="$month='07'">
				<xsl:text>July </xsl:text>
			</xsl:when>
			<xsl:when test="$month='08'">
				<xsl:text>August </xsl:text>
			</xsl:when>
			<xsl:when test="$month='09'">
				<xsl:text>September </xsl:text>
			</xsl:when>
			<xsl:when test="$month='10'">
				<xsl:text>October </xsl:text>
			</xsl:when>
			<xsl:when test="$month='11'">
				<xsl:text>November </xsl:text>
			</xsl:when>
			<xsl:when test="$month='12'">
				<xsl:text>December </xsl:text>
			</xsl:when>
		</xsl:choose>
		<!-- day -->
		<xsl:choose>
			<xsl:when test='substring ($date, 7, 1)="0"'>
				<xsl:value-of select="substring ($date, 8, 1)" />
				<xsl:text>, </xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="substring ($date, 7, 2)" />
				<xsl:text>, </xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		<!-- year -->
		<xsl:value-of select="substring ($date, 1, 4)" />
		<!-- time and US timezone -->
		<xsl:if test="string-length($date) > 8">
			<xsl:text>, </xsl:text>
			<!-- time -->
			<xsl:variable name="time">
				<xsl:value-of select="substring($date,9,6)" />
			</xsl:variable>
			<xsl:variable name="hh">
				<xsl:value-of select="substring($time,1,2)" />
			</xsl:variable>
			<xsl:variable name="mm">
				<xsl:value-of select="substring($time,3,2)" />
			</xsl:variable>
			<xsl:variable name="ss">
				<xsl:value-of select="substring($time,5,2)" />
			</xsl:variable>
			<xsl:if test="string-length($hh)&gt;1">
				<xsl:value-of select="$hh" />
				<xsl:if
					test="string-length($mm)&gt;1 and not(contains($mm,'-')) and not (contains($mm,'+'))">
					<xsl:text>:</xsl:text>
					<xsl:value-of select="$mm" />
					<xsl:if
						test="string-length($ss)&gt;1 and not(contains($ss,'-')) and not (contains($ss,'+'))">
						<xsl:text>:</xsl:text>
						<xsl:value-of select="$ss" />
					</xsl:if>
				</xsl:if>
			</xsl:if>
			<!-- time zone -->
			<xsl:variable name="tzon">
				<xsl:choose>
					<xsl:when test="contains($date,'+')">
						<xsl:text>+</xsl:text>
						<xsl:value-of select="substring-after($date, '+')" />
					</xsl:when>
					<xsl:when test="contains($date,'-')">
						<xsl:text>-</xsl:text>
						<xsl:value-of select="substring-after($date, '-')" />
					</xsl:when>
				</xsl:choose>
			</xsl:variable>
			<xsl:choose>
				<!-- reference: http://www.timeanddate.com/library/abbreviations/timezones/na/ -->
				<xsl:when test="$tzon = '-0500' ">
					<xsl:text>, EST</xsl:text>
				</xsl:when>
				<xsl:when test="$tzon = '-0600' ">
					<xsl:text>, CST</xsl:text>
				</xsl:when>
				<xsl:when test="$tzon = '-0700' ">
					<xsl:text>, MST</xsl:text>
				</xsl:when>
				<xsl:when test="$tzon = '-0800' ">
					<xsl:text>, PST</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text> </xsl:text>
					<xsl:value-of select="$tzon" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	<!-- convert to lower case -->
	<xsl:template name="caseDown">
		<xsl:param name="data" />
		<xsl:if test="$data">
			<xsl:value-of
				select="translate($data, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')" />
		</xsl:if>
	</xsl:template>
	<!-- convert to upper case -->
	<xsl:template name="caseUp">
		<xsl:param name="data" />
		<xsl:if test="$data">
			<xsl:value-of
				select="translate($data,'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ')" />
		</xsl:if>
	</xsl:template>
	<!-- convert first character to upper case -->
	<xsl:template name="firstCharCaseUp">
		<xsl:param name="data" />
		<xsl:if test="$data">
			<xsl:call-template name="caseUp">
				<xsl:with-param name="data" select="substring($data,1,1)" />
			</xsl:call-template>
			<xsl:value-of select="substring($data,2)" />
		</xsl:if>
	</xsl:template>
	<!-- show-noneFlavor -->
	<xsl:template name="show-noneFlavor">
		<xsl:param name="nf" />
		<xsl:choose>
			<xsl:when test=" $nf = 'NI' ">
				<xsl:text>no information</xsl:text>
			</xsl:when>
			<xsl:when test=" $nf = 'INV' ">
				<xsl:text>invalid</xsl:text>
			</xsl:when>
			<xsl:when test=" $nf = 'MSK' ">
				<xsl:text>masked</xsl:text>
			</xsl:when>
			<xsl:when test=" $nf = 'NA' ">
				<xsl:text>not applicable</xsl:text>
			</xsl:when>
			<xsl:when test=" $nf = 'UNK' ">
				<xsl:text>unknown</xsl:text>
			</xsl:when>
			<xsl:when test=" $nf = 'OTH' ">
				<xsl:text>other</xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:template>


	<!-- Format first(given) and last (family) names -->
	<xsl:template name="formatProviderName">
		<xsl:param name="row" />
		<xsl:param name="first" />
		<xsl:param name="last" />
		<xsl:choose>
			<xsl:when test="$first and $last">
				<xsl:value-of select="$last" />
				<text>, </text>
				<xsl:value-of select="$first" />
			</xsl:when>
			<xsl:otherwise />
		</xsl:choose>
	</xsl:template>

	<xsl:template name="formatComments">
		<xsl:param name="comments" />
		<div>
			<table class="comments">
				<tbody>
					<tr>
						<td>
							<img src="res/C32_notice.jpg" alt="NOTE:" />
							<xsl:value-of select="$comments" />
						</td>
					</tr>
				</tbody>
			</table>
			<br />
		</div>
	</xsl:template>

	<xsl:template name="formatDate">
		<xsl:param name="date" />
		<xsl:if test="string-length($date)>0">
			<xsl:variable name="month" select="substring ($date, 5, 2)" />
			<xsl:choose>
				<xsl:when test="$month='01'">
					<xsl:text>Jan </xsl:text>
				</xsl:when>
				<xsl:when test="$month='02'">
					<xsl:text>Feb </xsl:text>
				</xsl:when>
				<xsl:when test="$month='03'">
					<xsl:text>Mar </xsl:text>
				</xsl:when>
				<xsl:when test="$month='04'">
					<xsl:text>Apr </xsl:text>
				</xsl:when>
				<xsl:when test="$month='05'">
					<xsl:text>May </xsl:text>
				</xsl:when>
				<xsl:when test="$month='06'">
					<xsl:text>Jun </xsl:text>
				</xsl:when>
				<xsl:when test="$month='07'">
					<xsl:text>Jul </xsl:text>
				</xsl:when>
				<xsl:when test="$month='08'">
					<xsl:text>Aug </xsl:text>
				</xsl:when>
				<xsl:when test="$month='09'">
					<xsl:text>Sep </xsl:text>
				</xsl:when>
				<xsl:when test="$month='10'">
					<xsl:text>Oct </xsl:text>
				</xsl:when>
				<xsl:when test="$month='11'">
					<xsl:text>Nov </xsl:text>
				</xsl:when>
				<xsl:when test="$month='12'">
					<xsl:text>Dec </xsl:text>
				</xsl:when>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test='substring ($date, 7, 1)="0"'>
					<xsl:value-of select="substring ($date, 8, 1)" />
					<xsl:text>, </xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="substring ($date, 7, 2)" />
					<xsl:text>, </xsl:text>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:value-of select="substring ($date, 3, 2)" />
		</xsl:if>
	</xsl:template>

	<!-- Format Date outputs a date in Month Day, Year form e.g., 19991207 ==> 
		December 07, 1999 -->
	<xsl:template name="formatDateFull">
		<xsl:param name="date" />
		<xsl:variable name="month" select="substring ($date, 5, 2)" />
		<xsl:choose>
			<xsl:when test="$month='01'">
				<xsl:text>January </xsl:text>
			</xsl:when>
			<xsl:when test="$month='02'">
				<xsl:text>February </xsl:text>
			</xsl:when>
			<xsl:when test="$month='03'">
				<xsl:text>March </xsl:text>
			</xsl:when>
			<xsl:when test="$month='04'">
				<xsl:text>April </xsl:text>
			</xsl:when>
			<xsl:when test="$month='05'">
				<xsl:text>May </xsl:text>
			</xsl:when>
			<xsl:when test="$month='06'">
				<xsl:text>June </xsl:text>
			</xsl:when>
			<xsl:when test="$month='07'">
				<xsl:text>July </xsl:text>
			</xsl:when>
			<xsl:when test="$month='08'">
				<xsl:text>August </xsl:text>
			</xsl:when>
			<xsl:when test="$month='09'">
				<xsl:text>September </xsl:text>
			</xsl:when>
			<xsl:when test="$month='10'">
				<xsl:text>October </xsl:text>
			</xsl:when>
			<xsl:when test="$month='11'">
				<xsl:text>November </xsl:text>
			</xsl:when>
			<xsl:when test="$month='12'">
				<xsl:text>December </xsl:text>
			</xsl:when>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test='substring ($date, 7, 1)="0"'>
				<xsl:value-of select="substring ($date, 8, 1)" />
				<xsl:text>, </xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="substring ($date, 7, 2)" />
				<xsl:text>, </xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:value-of select="substring ($date, 1, 4)" />
	</xsl:template>
</xsl:stylesheet>