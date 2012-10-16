<?xml version="1.0"?>

<!--
  A transformation for mapping an XML encoded HL7 message into an er7 encoded message.
  The transformation also handles the Batch (BHS and BTS) and File (FHS and FTS) segments,
  BUT IT DOES NOT HANDLE A BATCHED SET OF MESSAGES, i.e., it does not handle a properly
  constructed XML batch file with HL7 messages wrapped in CDATA.
 -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="text" encoding="UTF-8"/>

  <xsl:variable name="nl"><xsl:text>&#13;</xsl:text></xsl:variable>

  <!-- MSH special chars -->
  <xsl:variable name="fieldSeparatorMessage">
    <xsl:value-of select="normalize-space(//MSH/MSH.1)"/>
  </xsl:variable>
  <xsl:variable name="msh2">
    <xsl:value-of select="normalize-space(//MSH/MSH.2)"/>
  </xsl:variable>
  <xsl:variable name="compSeparatorMessage">
    <xsl:value-of select="substring($msh2, 1, 1)"/>
  </xsl:variable>
  <xsl:variable name="repCharMessage">
    <xsl:value-of select="substring($msh2, 2, 1)"/>
  </xsl:variable>
  <xsl:variable name="escapeCharMessage">
    <xsl:value-of select="substring($msh2, 3, 1)"/>
  </xsl:variable>
  <!-- special chars, like "&", will be correctly handled by the xml parser -->
  <xsl:variable name="subSeparatorMessage">
    <xsl:value-of select="substring($msh2, 4, 1)"/>
  </xsl:variable>
  <!-- BHS special chars -->
  <xsl:variable name="fieldSeparatorBatch">
    <xsl:value-of select="normalize-space(//BHS/BHS.1)"/>
  </xsl:variable>
  <xsl:variable name="bhs2">
    <xsl:value-of select="normalize-space(//BHS/BHS.2)"/>
  </xsl:variable>
  <xsl:variable name="compSeparatorBatch">
    <xsl:value-of select="substring($bhs2, 1, 1)"/>
  </xsl:variable>
  <xsl:variable name="repCharBatch">
    <xsl:value-of select="substring($bhs2, 2, 1)"/>
  </xsl:variable>
  <xsl:variable name="escapeCharBatch">
    <xsl:value-of select="substring($bhs2, 3, 1)"/>
  </xsl:variable>
  <!-- special chars, like "&", will be correctly handled by the xml parser -->
  <xsl:variable name="subSeparatorBatch">
    <xsl:value-of select="substring($bhs2, 4, 1)"/>
  </xsl:variable>
  <!-- FHS special chars -->
  <xsl:variable name="fieldSeparatorFile">
    <xsl:value-of select="normalize-space(//FHS/FHS.1)"/>
  </xsl:variable>
  <xsl:variable name="fhs2">
    <xsl:value-of select="normalize-space(//FHS/FHS.2)"/>
  </xsl:variable>
  <xsl:variable name="compSeparatorFile">
    <xsl:value-of select="substring($fhs2, 1, 1)"/>
  </xsl:variable>
  <xsl:variable name="repCharFile">
    <xsl:value-of select="substring($fhs2, 2, 1)"/>
  </xsl:variable>
  <xsl:variable name="escapeCharFile">
    <xsl:value-of select="substring($fhs2, 3, 1)"/>
  </xsl:variable>
  <!-- special chars, like "&", will be correctly handled by the xml parser -->
  <xsl:variable name="subSeparatorFile">
    <xsl:value-of select="substring($fhs2, 4, 1)"/>
  </xsl:variable>

  <!-- start -->
  <xsl:template match="/">
    <xsl:for-each select="child::*">
      <xsl:call-template name="messageType"/>
    </xsl:for-each>
  </xsl:template>

  <!-- ......message type.......  -->
  <xsl:template name="messageType">
    <!-- process each Segment or group -->
    <xsl:for-each select="child::*">
      <xsl:call-template name="segmentOrGroup"/>
    </xsl:for-each>
  </xsl:template>

  <!-- ......SegmentOrGroup.......  -->
  <xsl:template name="segmentOrGroup">
    <xsl:choose>
      <!-- Segment names have to be 3 chars. If the name is longer than
          3 chars, it should be a group name (not part of er7 encoding) -->
      <xsl:when test="string-length(local-name()) &gt; 3">
        <xsl:for-each select="child::*">
          <xsl:call-template name="segmentOrGroup"/>
        </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>
        <!-- Segment -->
        <xsl:choose>
          <xsl:when test="count(child::*) &gt; 0">
            <xsl:call-template name="segment"/>
            <xsl:value-of select="$nl"/>
          </xsl:when>
        </xsl:choose>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- ......Segment.......  -->
  <xsl:template name="segment">
    <xsl:variable name="segName">
      <xsl:value-of select="local-name()"/>
    </xsl:variable>
     <!-- dump Segment name -->
    <xsl:value-of select="$segName"/>

    <!-- process each Field -->
    <xsl:for-each select="child::*">
      <xsl:variable name="fieldName">
        <xsl:value-of select="local-name()"/>
      </xsl:variable>
      <!-- name of immediate sibling  (siblings are returned in reverse doc order) -->
      <xsl:variable name="siblingName">
        <xsl:value-of select="local-name(preceding-sibling::*[1])"/>
      </xsl:variable>

      <xsl:choose>
        <!-- first 2 Fields of MSH contain special chars picked up above -->
        <xsl:when test="$segName = 'MSH' and $fieldName = 'MSH.1'">
          <xsl:if test="$fieldSeparatorMessage = '' or
                $compSeparatorMessage = '' or
                $repCharMessage = '' or
                $escapeCharMessage = '' or
                $subSeparatorMessage = ''">
            *****ERROR****
            CANNOT TRANSLATE CORRECTLY!  ONE OR MORE SPECIAL CHARS EMPTY.
            *****ERROR****
          </xsl:if>
          <!-- dump special chars -->
          <xsl:value-of select="$fieldSeparatorMessage"/>
          <xsl:value-of select="$compSeparatorMessage"/>
          <xsl:value-of select="$repCharMessage"/>
          <xsl:value-of select="$escapeCharMessage"/>
          <xsl:value-of select="$subSeparatorMessage"/>
        </xsl:when>
        <xsl:when test="$segName = 'MSH' and $fieldName = 'MSH.2'">
          <!-- skip field -->
        </xsl:when>
        <!-- first 2 Fields of BHS contain special chars picked up above -->
        <xsl:when test="$segName = 'BHS' and $fieldName = 'BHS.1'">
          <xsl:if test="$fieldSeparatorBatch = '' or
            $compSeparatorBatch = '' or
            $repCharBatch = '' or
            $escapeCharBatch = '' or
            $subSeparatorBatch = ''">
            *****ERROR****
            CANNOT TRANSLATE CORRECTLY!  ONE OR MORE SPECIAL CHARS EMPTY.
            *****ERROR****
          </xsl:if>
          <!-- dump special chars -->
          <xsl:value-of select="$fieldSeparatorBatch"/>
          <xsl:value-of select="$compSeparatorBatch"/>
          <xsl:value-of select="$repCharBatch"/>
          <xsl:value-of select="$escapeCharBatch"/>
          <xsl:value-of select="$subSeparatorBatch"/>
        </xsl:when>
        <xsl:when test="$segName = 'BHS' and $fieldName = 'BHS.2'">
          <!-- skip field -->
        </xsl:when>
        <!-- first 2 Fields of FHS contain special chars picked up above -->
        <xsl:when test="$segName = 'FHS' and $fieldName = 'FHS.1'">
          <xsl:if test="$fieldSeparatorFile = '' or
            $compSeparatorFile = '' or
            $repCharFile = '' or
            $escapeCharFile = '' or
            $subSeparatorFile = ''">
            *****ERROR****
            CANNOT TRANSLATE CORRECTLY!  ONE OR MORE SPECIAL CHARS EMPTY.
            *****ERROR****
          </xsl:if>
          <!-- dump special chars -->
          <xsl:value-of select="$fieldSeparatorFile"/>
          <xsl:value-of select="$compSeparatorFile"/>
          <xsl:value-of select="$repCharFile"/>
          <xsl:value-of select="$escapeCharFile"/>
          <xsl:value-of select="$subSeparatorFile"/>
        </xsl:when>
        <xsl:when test="$segName = 'FHS' and $fieldName = 'FHS.2'">
          <!-- skip field -->
        </xsl:when>
        <xsl:otherwise>  <!-- if it's not 1 of 1st 2 flds of MSH, BHS, or FHS -->
          <!-- flag batch and file segments -->
          <xsl:variable name="batchSegment">
            <xsl:choose>
              <xsl:when test="$segName = 'BHS' or $segName = 'BTS'">
                <xsl:value-of select="'true'"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="'false'"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>
          <xsl:variable name="fileSegment">
            <xsl:choose>
              <xsl:when test="$segName = 'FHS' or $segName = 'FTS'">
                <xsl:value-of select="'true'"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="'false'"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>

          <!-- set special chars to use -->
          <xsl:variable name="fieldSeparator">
            <xsl:choose>
              <xsl:when test="$batchSegment = 'true'">
                <xsl:value-of select="$fieldSeparatorBatch"/>
              </xsl:when>
              <xsl:when test="$fileSegment = 'true'">
                <xsl:value-of select="$fieldSeparatorFile"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="$fieldSeparatorMessage"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>
          <xsl:variable name="compSeparator">
            <xsl:choose>
              <xsl:when test="$batchSegment = 'true'">
                <xsl:value-of select="$compSeparatorBatch"/>
              </xsl:when>
              <xsl:when test="$fileSegment = 'true'">
                <xsl:value-of select="$compSeparatorFile"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="$compSeparatorMessage"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>
          <xsl:variable name="repChar">
            <xsl:choose>
              <xsl:when test="$batchSegment = 'true'">
                <xsl:value-of select="$repCharBatch"/>
              </xsl:when>
              <xsl:when test="$fileSegment = 'true'">
                <xsl:value-of select="$repCharFile"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="$repCharMessage"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>
          <xsl:variable name="escapeChar">
            <xsl:choose>
              <xsl:when test="$batchSegment = 'true'">
                <xsl:value-of select="$escapeCharBatch"/>
              </xsl:when>
              <xsl:when test="$fileSegment = 'true'">
                <xsl:value-of select="$escapeCharFile"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="$escapeCharMessage"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>
          <xsl:variable name="subSeparator">
            <xsl:choose>
              <xsl:when test="$batchSegment = 'true'">
                <xsl:value-of select="$subSeparatorBatch"/>
              </xsl:when>
              <xsl:when test="$fileSegment = 'true'">
                <xsl:value-of select="$subSeparatorFile"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="$subSeparatorMessage"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>

          <!-- get this Field's sequence number -->
          <xsl:variable name="fieldNum">
            <xsl:call-template name="getTrailingNumber">
              <xsl:with-param name="elementName" select="$fieldName"/>
            </xsl:call-template>
          </xsl:variable>

          <!-- get previous Field's sequence number -->
          <xsl:variable name="siblingNum">
            <xsl:choose>
              <!-- Field separators precede Field, so assign 1st 0 -->
              <xsl:when test="$siblingName = ''">
                <xsl:value-of select="0"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:call-template name="getTrailingNumber">
                  <xsl:with-param name="elementName" select="$siblingName"/>
                </xsl:call-template>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>

          <!-- use Field or repetition separator -->
          <xsl:variable name="separator">
            <xsl:choose>
              <xsl:when test="$fieldName = $siblingName">
                <xsl:value-of select="$repChar"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="$fieldSeparator"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>

          <!-- if it's a repeating Field, have to repeat separator once -->
          <xsl:variable name="separatorReps">
            <xsl:choose>
              <xsl:when test="$separator = $repChar">
                <xsl:value-of select="1"/>
              </xsl:when>
              <xsl:otherwise>    <!-- new Field -->
                <xsl:value-of select="$fieldNum - $siblingNum"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>

          <!-- add separators -->
          <xsl:call-template name="dumpChar">
            <xsl:with-param name="char" select="$separator"/>
            <xsl:with-param name="count" select="$separatorReps"/>
          </xsl:call-template>

          <!-- add Field after separator -->
          <xsl:call-template name="field">
            <xsl:with-param name="fieldSeparator" select="$fieldSeparator"/>
            <xsl:with-param name="compSeparator" select="$compSeparator"/>
            <xsl:with-param name="repChar" select="$repChar"/>
            <xsl:with-param name="escapeChar" select="$escapeChar"/>
            <xsl:with-param name="subSeparator" select="$subSeparator"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>
  </xsl:template>

  <!-- ......Field.......  -->
  <xsl:template name="field">
    <xsl:param name="fieldSeparator" select="''"/>
    <xsl:param name="compSeparator" select="''"/>
    <xsl:param name="repChar" select="''"/>
    <xsl:param name="escapeChar" select="''"/>
    <xsl:param name="subSeparator" select="''"/>

    <xsl:choose>
      <!-- see if the Field contains Components... -->
      <xsl:when test="child::*">
        <xsl:for-each select="child::*">
          <xsl:variable name="compName">
            <xsl:value-of select="local-name()"/>
          </xsl:variable>
          <!-- name of immediate sibling  (siblings are returned in reverse doc order) -->
          <xsl:variable name="siblingName">
            <xsl:value-of select="local-name(preceding-sibling::*[1])"/>
          </xsl:variable>

         <!-- Comp sequence number -->
          <xsl:variable name="compNum">
            <xsl:call-template name="getTrailingNumber">
              <xsl:with-param name="elementName" select="$compName"/>
            </xsl:call-template>
          </xsl:variable>

         <!-- immediate sibling of Comp sequence number -->
          <xsl:variable name="siblingNum">
            <xsl:choose>
              <!-- Comp separators follow Comp (assign 1 1st to avoid including before Comp) -->
              <xsl:when test="$siblingName = ''">
                <xsl:value-of select="1"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:call-template name="getTrailingNumber">
                  <xsl:with-param name="elementName" select="$siblingName"/>
                </xsl:call-template>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>

          <!-- add component separators -->
          <xsl:call-template name="dumpChar">
            <xsl:with-param name="char" select="$compSeparator"/>
            <xsl:with-param name="count" select="$compNum - $siblingNum"/>
          </xsl:call-template>

          <!-- add Component -->
          <xsl:call-template name="component">
            <xsl:with-param name="fieldSeparator" select="$fieldSeparator"/>
            <xsl:with-param name="compSeparator" select="$compSeparator"/>
            <xsl:with-param name="repChar" select="$repChar"/>
            <xsl:with-param name="escapeChar" select="$escapeChar"/>
            <xsl:with-param name="subSeparator" select="$subSeparator"/>
          </xsl:call-template>
        </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>  <!-- dump Field value -->
        <xsl:call-template name="dumpDataValue">
          <xsl:with-param name="dataValue" select="string()"/>
          <xsl:with-param name="fieldSeparator" select="$fieldSeparator"/>
          <xsl:with-param name="compSeparator" select="$compSeparator"/>
          <xsl:with-param name="repChar" select="$repChar"/>
          <xsl:with-param name="escapeChar" select="$escapeChar"/>
          <xsl:with-param name="subSeparator" select="$subSeparator"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- ......Component.......  -->
  <xsl:template name="component">
    <xsl:param name="fieldSeparator" select="''"/>
    <xsl:param name="compSeparator" select="''"/>
    <xsl:param name="repChar" select="''"/>
    <xsl:param name="escapeChar" select="''"/>
    <xsl:param name="subSeparator" select="''"/>

    <xsl:choose>
      <!-- see if the Component contains SubComponents... -->
      <xsl:when test="child::*">
        <xsl:for-each select="child::*">
          <xsl:variable name="subName">
            <xsl:value-of select="local-name()"/>
          </xsl:variable>
          <!-- name of immediate sibling  (siblings are returned in reverse doc order) -->
          <xsl:variable name="siblingName">
            <xsl:value-of select="local-name(preceding-sibling::*[1])"/>
          </xsl:variable>

          <!-- SubComp sequence number -->
          <xsl:variable name="subNum">
            <xsl:call-template name="getTrailingNumber">
              <xsl:with-param name="elementName" select="$subName"/>
            </xsl:call-template>
          </xsl:variable>

          <!-- SubComp sibling's sequence number -->
          <xsl:variable name="siblingNum">
            <xsl:choose>
              <xsl:when test="$siblingName = ''">
                <xsl:value-of select="1"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:call-template name="getTrailingNumber">
                  <xsl:with-param name="elementName" select="$siblingName"/>
                </xsl:call-template>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>

          <!-- add SubComponent separators -->
          <xsl:call-template name="dumpChar">
            <xsl:with-param name="char" select="$subSeparator"/>
            <xsl:with-param name="count" select="$subNum - $siblingNum"/>
          </xsl:call-template>

          <!-- add SubComp -->
          <xsl:call-template name="subComponent">
            <!-- <xsl:with-param name="dataValue" select="string()"/> -->
            <xsl:with-param name="fieldSeparator" select="$fieldSeparator"/>
            <xsl:with-param name="compSeparator" select="$compSeparator"/>
            <xsl:with-param name="repChar" select="$repChar"/>
            <xsl:with-param name="escapeChar" select="$escapeChar"/>
            <xsl:with-param name="subSeparator" select="$subSeparator"/>
          </xsl:call-template>
        </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>  <!-- add Component value -->
        <xsl:call-template name="dumpDataValue">
          <xsl:with-param name="dataValue" select="string()"/>
          <xsl:with-param name="fieldSeparator" select="$fieldSeparator"/>
          <xsl:with-param name="compSeparator" select="$compSeparator"/>
          <xsl:with-param name="repChar" select="$repChar"/>
          <xsl:with-param name="escapeChar" select="$escapeChar"/>
          <xsl:with-param name="subSeparator" select="$subSeparator"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- ......SubComponent.......  -->
  <xsl:template name="subComponent">
    <xsl:param name="fieldSeparator" select="''"/>
    <xsl:param name="compSeparator" select="''"/>
    <xsl:param name="repChar" select="''"/>
    <xsl:param name="escapeChar" select="''"/>
    <xsl:param name="subSeparator" select="''"/>

    <!-- add SubComp value -->
    <xsl:call-template name="dumpDataValue">
      <xsl:with-param name="dataValue" select="string()"/>
      <xsl:with-param name="fieldSeparator" select="$fieldSeparator"/>
      <xsl:with-param name="compSeparator" select="$compSeparator"/>
      <xsl:with-param name="repChar" select="$repChar"/>
      <xsl:with-param name="escapeChar" select="$escapeChar"/>
      <xsl:with-param name="subSeparator" select="$subSeparator"/>
    </xsl:call-template>
  </xsl:template>

  <!-- pull number from string of form: string.Number -->
  <xsl:template name="getTrailingNumber">
    <xsl:param name="elementName" select="''"/>
    <!-- if we don't grab a number, NaN is returned.
      all numeric comparisons with NaN return false, should be what we want -->
    <xsl:value-of select="substring-after($elementName, '.')"/>
  </xsl:template>

  <!-- dump 'char' 'count' times-->
  <xsl:template name="dumpChar">
    <xsl:param name="char" select="''"/>
    <xsl:param name="count" select="0"/>

    <xsl:if test="$count &gt; 0">
      <xsl:value-of select="$char"/>
      <xsl:call-template name="dumpChar">
        <xsl:with-param name="char" select="$char"/>
        <xsl:with-param name="count" select="$count - 1"/>
      </xsl:call-template>
    </xsl:if>
    <xsl:if test="$count &lt; 0">
      <xsl:value-of select="$char"/>
    </xsl:if>
  </xsl:template>

  <!-- before values are included in the output stream, they're normalized
        (leading and trailing white space removed) and the special chars captured
        above have to be escaped when part of a data value. -->
  <xsl:template name="dumpDataValue">
    <xsl:param name="dataValue" select="''"/>
    <xsl:param name="init" select="'true'"/>
    <xsl:param name="fieldSeparator" select="''"/>
    <xsl:param name="compSeparator" select="''"/>
    <xsl:param name="repChar" select="''"/>
    <xsl:param name="escapeChar" select="''"/>
    <xsl:param name="subSeparator" select="''"/>

    <!-- first, normalize -->
    <xsl:variable name="nvalue">
      <xsl:value-of select="normalize-space($dataValue)"/>
    </xsl:variable>

    <!-- escape char has to be escaped first (and only once) -->
    <xsl:variable name="ecValue">
      <xsl:choose>
        <xsl:when test="$init = 'true' and contains($nvalue, $escapeChar)">
          <xsl:call-template name="escapeescapeChars">
            <xsl:with-param name="inString" select="$nvalue"/>
            <xsl:with-param name="escapeChar" select="$escapeChar"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <!-- no change to value  -->
          <xsl:value-of select="$nvalue"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <!-- escape Field separator -->
    <xsl:variable name="fsValue">
      <xsl:choose>
        <xsl:when test="$fieldSeparator and contains($ecValue, $fieldSeparator)">
          <xsl:variable name="newString">
            <xsl:call-template name="replaceWithEscapeSequence">
              <xsl:with-param name="inString" select="$ecValue"/>
              <xsl:with-param name="newChar" select="'F'"/>
              <xsl:with-param name="oldChar" select="$fieldSeparator"/>
              <xsl:with-param name="escapeChar" select="$escapeChar"/>
            </xsl:call-template>
          </xsl:variable>
          <!-- there may be additional fieldSepartors to escape  -->
           <xsl:call-template name="dumpDataValue">
            <xsl:with-param name="dataValue" select="$newString"/>
            <xsl:with-param name="init" select="'false'"/>
            <xsl:with-param name="fieldSeparator" select="$fieldSeparator"/>
            <xsl:with-param name="compSeparator" select="''"/>
            <xsl:with-param name="repChar" select="''"/>
            <xsl:with-param name="escapeChar" select="$escapeChar"/>
            <xsl:with-param name="subSeparator" select="''"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <!-- no change to value -->
          <xsl:value-of select="$ecValue"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <!-- escape Component separator -->
    <xsl:variable name="csValue">
      <xsl:choose>
        <xsl:when test="$compSeparator and contains($fsValue, $compSeparator)">
          <xsl:variable name="newString">
            <xsl:call-template name="replaceWithEscapeSequence">
              <xsl:with-param name="inString" select="$fsValue"/>
              <xsl:with-param name="newChar" select="'S'"/>
              <xsl:with-param name="oldChar" select="$compSeparator"/>
              <xsl:with-param name="escapeChar" select="$escapeChar"/>
            </xsl:call-template>
          </xsl:variable>
          <!-- there may be additional comp separtors to escape  -->
           <xsl:call-template name="dumpDataValue">
            <xsl:with-param name="dataValue" select="$newString"/>
            <xsl:with-param name="init" select="'false'"/>
            <xsl:with-param name="fieldSeparator" select="''"/>
            <xsl:with-param name="compSeparator" select="$compSeparator"/>
            <xsl:with-param name="repChar" select="''"/>
            <xsl:with-param name="escapeChar" select="$escapeChar"/>
            <xsl:with-param name="subSeparator" select="''"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <!-- no change to value -->
          <xsl:value-of select="$fsValue"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <!-- escape SubComponent separator -->
    <xsl:variable name="ssValue">
      <xsl:choose>
        <xsl:when test="$subSeparator and contains($csValue, $subSeparator)">
          <xsl:variable name="newString">
            <xsl:call-template name="replaceWithEscapeSequence">
              <xsl:with-param name="inString" select="$csValue"/>
              <xsl:with-param name="newChar" select="'T'"/>
              <xsl:with-param name="oldChar" select="$subSeparator"/>
              <xsl:with-param name="escapeChar" select="$escapeChar"/>
            </xsl:call-template>
          </xsl:variable>
          <!-- there may be additional subcomp separtors to escape  -->
          <xsl:call-template name="dumpDataValue">
            <xsl:with-param name="dataValue" select="$newString"/>
            <xsl:with-param name="init" select="'false'"/>
            <xsl:with-param name="fieldSeparator" select="''"/>
            <xsl:with-param name="compSeparator" select="''"/>
            <xsl:with-param name="repChar" select="''"/>
            <xsl:with-param name="escapeChar" select="$escapeChar"/>
            <xsl:with-param name="subSeparator" select="$subSeparator"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <!-- no change to value -->
          <xsl:value-of select="$csValue"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <!-- escape repetition char -->
    <xsl:variable name="rcValue">
      <xsl:choose>
        <xsl:when test="$repChar and contains($ssValue, $repChar)">
          <xsl:variable name="newString">
            <xsl:call-template name="replaceWithEscapeSequence">
              <xsl:with-param name="inString" select="$ssValue"/>
              <xsl:with-param name="newChar" select="'R'"/>
              <xsl:with-param name="oldChar" select="$repChar"/>
              <xsl:with-param name="escapeChar" select="$escapeChar"/>
            </xsl:call-template>
          </xsl:variable>
          <!-- there may be additional rep chars to escape  -->
           <xsl:call-template name="dumpDataValue">
            <xsl:with-param name="dataValue" select="$newString"/>
            <xsl:with-param name="init" select="'false'"/>
            <xsl:with-param name="fieldSeparator" select="''"/>
            <xsl:with-param name="compSeparator" select="''"/>
            <xsl:with-param name="repChar" select="$repChar"/>
            <xsl:with-param name="escapeChar" select="$escapeChar"/>
            <xsl:with-param name="subSeparator" select="''"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <!-- no change to value -->
          <xsl:value-of select="$ssValue"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <!-- return new value -->
    <xsl:value-of select="$rcValue"/>
  </xsl:template>

  <!-- escape escape characters in input string.
      -->
  <xsl:template name="escapeescapeChars">
    <xsl:param name="inString" select="''"/>
    <xsl:param name="escapeChar" select="''"/>
    <xsl:variable name="processedString">
      <xsl:if test="contains($inString, $escapeChar)">
        <!-- split input string into head and tail -->
        <xsl:variable name="head"
          select="concat(substring-before($inString, '\'), '\')"/>
        <xsl:variable name="tail" select="substring-after($inString, '\')"/>
        <!-- process head -->
        <xsl:variable name="escapedString">
          <xsl:call-template name="replaceWithEscapeSequence">
            <xsl:with-param name="inString" select="$head"/>
            <xsl:with-param name="newChar" select="'E'"/>
            <xsl:with-param name="oldChar" select="$escapeChar"/>
            <xsl:with-param name="escapeChar" select="$escapeChar"/>
          </xsl:call-template>
        </xsl:variable>

        <!-- set value to value of escaped string and append tail
            if it doesn't contain any escape chars -->
        <xsl:choose>
          <xsl:when test="contains($tail, $escapeChar)">
            <xsl:value-of select="$escapedString"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="concat($escapedString, $tail)"/>
          </xsl:otherwise>
        </xsl:choose>

        <xsl:call-template name="escapeescapeChars">
          <xsl:with-param name="inString" select="$tail"/>
          <xsl:with-param name="escapeChar" select="$escapeChar"/>
        </xsl:call-template>
      </xsl:if>
    </xsl:variable>

    <!-- dump value -->
    <xsl:choose>
      <!-- processed string should be empty if no escape chars in input string -->
      <xsl:when test="$processedString = ''">
        <!-- <xsl:value-of select="$inString"/> -->
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$processedString"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- replace first occurrance of 'oldChar' in 'inString' with 'escapeString'
        (use 'newChar' to build escapeString) -->
  <xsl:template name="replaceWithEscapeSequence">
    <xsl:param name="inString" select="''"/>
    <xsl:param name="newChar" select="''"/>
    <xsl:param name="oldChar" select="''"/>
    <xsl:param name="escapeChar" select="''"/>

    <xsl:variable name="escapeString">
      <xsl:value-of select="$escapeChar"/>
      <xsl:value-of select="$newChar"/>
      <xsl:value-of select="$escapeChar"/>
    </xsl:variable>

    <xsl:variable name="newStringHead" select="substring-before($inString, $oldChar)"/>
    <xsl:variable name="newStringTail" select="substring-after($inString, $oldChar)"/>

    <!-- return new string -->
    <xsl:value-of select="concat($newStringHead, $escapeString, $newStringTail)"/>
  </xsl:template>

</xsl:stylesheet>
