<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>  
            <head>  
                <title><xsl:value-of select="view/header/title"/></title>  
            </head>  
            <body>  
            	<form>
            		<table>
	            		<xsl:attribute name="name"><xsl:value-of select="/view/body/form/name"/></xsl:attribute>
	            		<xsl:attribute name="action"><xsl:value-of select="/view/body/form/action"/></xsl:attribute>
	            		<xsl:attribute name="method"><xsl:value-of select="/view/body/form/method"/></xsl:attribute>
            			<xsl:for-each select="view/body/form/textView">
            			<tr>
            				<td><xsl:value-of select="label"/></td>
            				<td><xsl:value-of select="value"/></td>
            			</tr>
            			</xsl:for-each>
         
            		</table>
            		<input>
            			<xsl:attribute name="type">submit</xsl:attribute>
            			<xsl:attribute name="value"><xsl:value-of select="view/body/form/buttonView/label"/></xsl:attribute>
            			<xsl:attribute name="name"><xsl:value-of select="view/body/form/buttonView/name"/></xsl:attribute>
            		</input>
            	</form>
            </body>
        </html>
	</xsl:template>
</xsl:stylesheet>