<#assign bean_prefix = "#{">
<html xmlns="http://www.w3.org/1999/xhtml" 
xmlns:h="http://java.sun.com/jsf/html" 
xmlns:f="http://java.sun.com/jsf/core" 
xmlns:p="http://primefaces.prime.com.tr/ui"
 xmlns:ui="http://java.sun.com/jsf/facelets"> 

<f:view contentType="text/html">
           <f:loadBundle basename="ApplicationResources" var="msgs"/> 
	    <link type="text/css" rel="stylesheet" href="../css/style.css" />
    <h:head>
    <title><h:outputText value="${bean_prefix}msgs.${resourceBundleTablePrefix}_PAGE_TITLE}" /></title>   
<script language="javascript">
 	        function confirmDelete(){
                 return confirm(' do you want to delete the selected row?');
            } 
function validateSearch(){
              if ( document.forms["${formid}"].elements["${formid}:searchfield"].value.length==0)
             {
                        alert('Please enter the value');
                        document.forms["${formid}"].elements["${formid}:searchfield"].focus();
                        return false;
                    }

             if ( document.forms["${formid}"].elements["${formid}:select"].value =='--Select the field--'){
                  alert('Please choose any one option');
                        document.forms["${formid}"].elements["${formid}:select"].focus();
                        return false;
                   }
            return true;
             }
			function validateClear(){
              
              document.forms["${formid}"].elements["${formid}:searchfield"].value="";
              document.forms["${formid}"].elements["${formid}:select"].value='--Select the field--';
              return true;
            }

    </script> 
</h:head>
    <body>
	 <h:form id="${formid}">
        
	   
 <div class="mainwrapper">
    	  <ui:include src="leftMenu.xhtml" />
    	 
			
			<div id="div1" class="rightcolumn">
				<div class="breadcrumb">
					<div class="breadcrumbtext">
						<h:outputText value="${bean_prefix}msgs.${resourceBundleTablePrefix}_PAGE_TITLE}-List" />
						
					</div>
			<p:spacer width="100" height="10" /> 
 
    

	 <p:panel>
	    <!--<f:facet name="header">
        <center><h:outputText value="${bean_prefix}msgs.${resourceBundleTablePrefix}_PAGE_TITLE}" /></center>
        </f:facet>-->
       
        <h:panelGrid columns="3">
                              <h:panelGroup>
                                    <b><p:inputText id="searchfield"
                                                value="${bean_prefix}${resourceBunbleTableintcapprefix}.fieldValue}" /></b>
                              </h:panelGroup>
                           
                              <h:panelGroup>
                                    <h:selectOneMenu id="select" value="${bean_prefix}${resourceBunbleTableintcapprefix}.columnValue}">
                                          <f:selectItems id="selectItem"
                                                value="${bean_prefix}${resourceBunbleTableintcapprefix}.columnNameList}" var="c"
                                                itemValue="${bean_prefix}c.columnValue}" />
                                    </h:selectOneMenu>
                              </h:panelGroup>
                              <h:panelGroup>
                                    <p:commandButton id="btnSearch" action="${bean_prefix}${resourceBunbleTableintcapprefix}.search}"
                                          value="SEARCH" onclick="return validateSearch();" ajax="false" />

					  <h:outputText value="&#160;" />
                                          <p:commandButton id="btnClear" action="${bean_prefix}${resourceBunbleTableintcapprefix}.getList}"
                                          value="Clear" onclick="return validateClear();" ajax="false" />
                              </h:panelGroup>
       </h:panelGrid>
           <h:outputText value="&#160;" />
           <p:messages for="${formid}" style="color:red;"/>
            <p:dataTable value="${bean_prefix}${resourceBunbleTableintcapprefix}.${resourceBunbleTablesmallrefix}DataModel}" var="${resourceBunbleTablesmallrefix}Row"  
                 paginator="true" rows="10"  paginatorPosition="bottom"
				 paginatorTemplate="{FirstPageLink} {PreviousPageLink} {PageLinks} {NextPageLink} {LastPageLink}"  
				  rowClasses="row1, row2" id="productList">
         
             <!--  <f:facet name="header">
                <h:outputText value="${bean_prefix}msgs.${resourceBundleTablePrefix}_LIST_PAGE_TITLE}"/>
            </f:facet>-->
             <#list dynamicTableContent as system>
				<#if system.getTag()=="DataWithHyperLinkTag">
				<@DataWithHyperLinkTag ColumnName=system.getColumnName() resourceBundleTablePrefix=system.getResourceBundleTablePrefix() psValueAtt=system.getDataTableRowVar() psAction=system.getBeanClassName() prefix=system.getPrefix()/>
				<#elseif system.getTag()=="DateTag">	
				<@DateTag ColumnName=system.getColumnName() resourceBundleTablePrefix=system.getResourceBundleTablePrefix() psValueAtt=system.getDataTableRowVar() DateFormat=system.getDateFormat() prefix=system.getPrefix()/>
				<#elseif system.getTag()=="PasswordTag">
				<@PasswordTag ColumnName=system.getColumnName() resourceBundleTablePrefix=system.getResourceBundleTablePrefix() 
psValueAtt=system.getDataTableRowVar() prefix=system.getPrefix()/>
				<#elseif system.getTag()=="AuditingTag">
				<@AuditingTag ColumnName=system.getColumnName() resourceBundleTablePrefix=system.getResourceBundleTablePrefix() 
psValueAtt=system.getDataTableRowVar() prefix=system.getPrefix()/>
				<#elseif system.getTag()=="DataTag">
				<@DataTag ColumnName=system.getColumnName() resourceBundleTablePrefix=system.getResourceBundleTablePrefix() 
psValueAtt=system.getDataTableRowVar() prefix=system.getPrefix()/>
			</#if>
			 </#list>
			 
			 
			 
 		<p:column>
                <f:facet name="header">
                    <h:outputLabel value="${bean_prefix}msgs.${resourceBundleTablePrefix}_CAPTION_DELETE}" />
                </f:facet>
                <h:selectBooleanCheckbox style="align:center" id="delete" value="${bean_prefix}${dataTableVar}.toBeDeleted}" onclick="chkBoxStateChanged(this);"/>
            </p:column>

        </p:dataTable>                
                <center><h:panelGrid columns="2"  columnClasses="btnAdd,btnDelete" style="text-align: right" >
			<h:panelGroup>
                     <p:commandButton id="btnAdd"   style="left:80%"  action="${bean_prefix}${VOClassName}Bean.add}" value="${bean_prefix}msgs.${resourceBundleTablePrefix}_BUTTON_ADD}" ajax="false"/>
			</h:panelGroup>
		<h:panelGroup>
                     <p:commandButton id="btnDelete"  style="right:20%" action="${bean_prefix}${deleteAction}}" value="${bean_prefix}msgs.${resourceBundleTablePrefix}_BUTTON_DELETE}" onclick="return confirmDelete();" ajax="false"/>
			</h:panelGroup>
                   </h:panelGrid></center>
        
	<!--<h5 align="right"><a href ="Home.xhtml">Home</a></h5>-->
</p:panel>

		</div>
		</div>
		</div>
 </h:form> 

 
    </body>
</f:view>
</html>


<#macro DataWithHyperLinkTag ColumnName resourceBundleTablePrefix psValueAtt psAction prefix>
<p:column>
<f:facet name="header">
<b>
<h:outputText value="${bean_prefix}msgs.${resourceBundleTablePrefix}_${ColumnName}}"/>
</b>
</f:facet>
<${prefix}:commandLink action="${bean_prefix}${psAction}}" ajax="false">
<h:outputText value="${bean_prefix}${psValueAtt}}"/> 
</${prefix}:commandLink>
 </p:column>
</#macro>

<#macro DateTag ColumnName resourceBundleTablePrefix psValueAtt DateFormat prefix>
<p:column>
<f:facet name="header">
<b>
<h:outputText value="${bean_prefix}msgs.${resourceBundleTablePrefix}_${ColumnName}}"/>
</b>
</f:facet>
<${prefix}:outputText value="${bean_prefix}${psValueAtt}}">
<f:convertDateTime type="date" pattern="${DateFormat}"/>
</${prefix}:outputText>
</p:column>
</#macro>

<#macro PasswordTag ColumnName resourceBundleTablePrefix psValueAtt prefix>
<p:column>
<f:facet name="header">
<b>
<h:outputText value="${bean_prefix}msgs.${resourceBundleTablePrefix}_${ColumnName}}"/>
</b>
</f:facet>
<${prefix}:password readonly="true" value="${bean_prefix}${psValueAtt}}"/> 
 </p:column>
</#macro>


<#macro AuditingTag ColumnName resourceBundleTablePrefix psValueAtt prefix>
<${prefix}:inputHidden value="${bean_prefix}msgs.${resourceBundleTablePrefix}_${ColumnName}}"/>
<${prefix}:inputHidden value="${bean_prefix}${psValueAtt}}"/>
</#macro>

<#macro DataTag ColumnName resourceBundleTablePrefix psValueAtt prefix>
<p:column>
<f:facet name="header">
<b>
<h:outputText value="${bean_prefix}msgs.${resourceBundleTablePrefix}_${ColumnName}}"/>
</b>
</f:facet>
<${prefix}:outputText value="${bean_prefix}${psValueAtt}}"/>
</p:column>
</#macro>