<#assign bean_prefix = "#{">
<#assign EditReadOnlyTrue="disabled=\"true\"">
<#assign passwordReadOnlytrue="readonly =\"false\"">
<#assign passwordReadOnlyfalse="readonly =\"false\"">
<#assign NullReadonlyTrueNoDate="readonly=\"true\" style=\"background-color:#d3d3d3;\"">
<#assign NullReadonlyFalseDate=" readonly=\"false\" ">
<#assign NullReadonlyTrueDate="readonly=\"true\" style=\"background-color:#d3d3d3;\"  ">
<#assign NotNullandReadonlyfalse=" readonly=\"false\" ">

<#if page=="edit">
<#assign NotNull=" readonly=\"true\" style=\"background-color:#d3d3d3;\"">
<#elseif page=="add">
<#assign NotNull=" readonly=\"true\" ">
</#if>
<#if page=="edit">
<#assign EditReadOnlyTrue="  disabled=\"true\" ">
<#elseif page=="add">
<#assign EditReadOnlyTrue=" ">
</#if>
<#assign NotNullReadonlyfalseNoDate=" readonly=\"false\" ">
<#if page=="edit">
<#assign NotNullReadonlyTrueNoDate=" readonly=\"true\" style=\"background-color:#d3d3d3;\"  ">
<#elseif page=="add">
<#assign NotNullReadonlyTrueNoDate=" readonly=\"true\"  ">
</#if>
<#if page=="edit">
<#assign Disabled=" readonly=\"true\" style=\"background-color:#d3d3d3;\" ">
<#elseif page=="add">
<#assign Disabled=" ">
</#if>
<#assign NullReadonlyFalseNoDate="readonly=\"false\"">
<html xmlns="http://www.w3.org/1999/xhtml" 
xmlns:h="http://java.sun.com/jsf/html" 
xmlns:f="http://java.sun.com/jsf/core" 
xmlns:p="http://primefaces.prime.com.tr/ui"
 xmlns:ui="http://java.sun.com/jsf/facelets"> 
  <SCRIPT LANGUAGE="JavaScript">	
		function validation()
		{
		<@JavascriptValidator formName=MandatoryValidations.getFormName() columnName=MandatoryValidations.getColumnName() ControlType=MandatoryValidations.getControlType() LabelName=MandatoryValidations.getLabelName()/>
			return true;
		}
	</SCRIPT>

   <f:view contentType="text/html">
 	<h:head>
            <f:loadBundle basename="ApplicationResources" var="msgs"/>
        <h3><title><h:outputText value="${bean_prefix}msgs.${resourceBundleTablePrefix}_PAGE_TITLE}" /></title></h3>
	<link type="text/css" rel="stylesheet" href="../css/style.css" />
    </h:head>
 
  <!-- <f:view contentType="text/html">
	<h:head>
		<f:loadBundle basename="ApplicationResources" var="msgs" />
		
	</h:head>-->

	
    <body>
      <div class="mainwrapper">
		<ui:include src="leftMenu.xhtml" />
		
		<div id="div1" class="rightcolumn">
		<div class="breadcrumb">
		<div class="breadcrumbtext"> <h:outputText value="${bean_prefix}msgs.${resourceBundleTablePrefix}_${MODE}_PAGE_TITLE}" /></div>
	<p:spacer width="100" height="10" /> 

	

      
			
                    
                <p:messages style="color:red;"/>
               

                    <h:form id="${formid}">
		    <p:panel>
                        <table  class="AddEdit"  width="100%" cellpadding="5px" border="0"  cellspacing="0px">

<#list dynamicEditContent as system>


<#--
<#if system.getTag()=="prepareEditTag">
<@prepareEditTag isMandatory=system.getIsMandatory() ColumnName=system.getColumnName() resourceBundleTablePrefix=system.getResourceBundleTablePrefix() ControlType=system.getControlType() dateFormat=system.getDateFormat()/>
-->

<#if system.getTag()=="primeTag">
<tr >
<@primeTag isReadonly=system.getIsReadonly() isDate=system.getIsDate() pk=system.getPk() ColumnName=system.getColumnName()  ControlType=system.getControlType() dateFormat=system.getDateFormat() beanClassName=system.getBeanClassName() voInstanceName=system.getVoInstanceName() nullable=system.getNullable() dataLength=system.getDataLength() validation=system.getValidation() minDateFormat=system.getMinDateFormat() fromDate=system.getFromDate() toDate=system.getToDate() prefix=system.getPrefix() isMandatory=system.getIsMandatory() resourceBundleTablePrefix=system.getResourceBundleTablePrefix() outputLabelColumnName=system.getOutputLabelColumnName()/>
</tr>

<#elseif system.getTag()=="prepareRadioTag">
<tr >
<@prepareRadioTag isReadonly=system.getIsReadonly() ColumnName=system.getColumnName() beanClassName=system.getBeanClassName() voInstanceName=system.getVoInstanceName() prefix=system.getPrefix() LabelsRadio=system.getLabelsRadio() resourceBundleTablePrefix=system.getResourceBundleTablePrefix() isMandatory=system.getIsMandatory() ControlType=system.getControlType() dateFormat=system.getDateFormat() outputLabelColumnName=system.getOutputLabelColumnName()/>
</tr>
<#elseif system.getTag()=="prepareCheckBoxTag">
<tr  >
<@prepareCheckBoxTag isReadonly=system.getIsReadonly() ColumnName=system.getColumnName() beanClassName=system.getBeanClassName() voInstanceName=system.getVoInstanceName() prefix=system.getPrefix() resourceBundleTablePrefix=system.getResourceBundleTablePrefix() isMandatory=system.getIsMandatory() ControlType=system.getControlType() dateFormat=system.getDateFormat() outputLabelColumnName=system.getOutputLabelColumnName()/>
</tr>
<#elseif system.getTag()=="TextAreaTag">
<tr >
<@TextAreaTag isReadonly=system.getIsReadonly() cols=system.getCols() ColumnName=system.getColumnName() beanClassName=system.getBeanClassName() voInstanceName=system.getVoInstanceName() prefix=system.getPrefix() resourceBundleTablePrefix=system.getResourceBundleTablePrefix() isMandatory=system.getIsMandatory() ControlType=system.getControlType() dateFormat=system.getDateFormat() outputLabelColumnName=system.getOutputLabelColumnName()/>
</tr>
<#elseif system.getTag()=="EditPageStaticSelectTag">
<tr >
<@EditPageStaticSelectTag isReadonly=system.getIsReadonly() ColumnName=system.getColumnName() beanClassName=system.getBeanClassName() voInstanceName=system.getVoInstanceName() resourceBundleTablePrefix=system.getResourceBundleTablePrefix() isMandatory=system.getIsMandatory() ControlType=system.getControlType() dateFormat=system.getDateFormat() outputLabelColumnName=system.getOutputLabelColumnName()/>
</tr>
<#elseif system.getTag()=="EditAuditingTag">

<#if page=="edit">
<tr >
<@EditAuditingTag isReadonly=system.getIsReadonly() ColumnName=system.getColumnName() beanClassName=system.getBeanClassName() voInstanceName=system.getVoInstanceName() prefix=system.getPrefix() ControlType=system.getControlType() dateFormat=system.getDateFormat() resourceBundleTablePrefix=system.getResourceBundleTablePrefix() isMandatory=system.getIsMandatory() outputLabelColumnName=system.getOutputLabelColumnName()/>
</tr>
</#if>
<#elseif system.getTag() == "EditPageSelectTag">
<tr >
<@EditPageSelectTag isReadonly=system.getIsReadonly() ColumnName=system.getColumnName() beanClassName=system.getBeanClassName() voInstanceName=system.getVoInstanceName() varName=system.getVarName() resourceBundleTablePrefix=system.getResourceBundleTablePrefix() isMandatory=system.getIsMandatory() ControlType=system.getControlType() dateFormat=system.getDateFormat() outputLabelColumnName=system.getOutputLabelColumnName()/>
</tr>
</#if>

</#list>

</table><table align="center">                          
                          <tr><td><p:commandButton id="${AddEditBtnID}"  style="float:right" action="${bean_prefix}${AddEditAction}}" value="${bean_prefix}msgs.${resourceBundleTablePrefix}_BUTTON_${AddEditValue}}" onclick="return validation();" ajax="false"/>

                          <p:commandButton id="list" style="float:right"  action="${bean_prefix}${ListAction}}" value="${bean_prefix}msgs.${resourceBundleTablePrefix}_BUTTON_LIST}"  immediate="true" ajax="false"/>
</td></tr></table>

</p:panel>
                    </h:form>

             </div>
	     </div> 
	     </div>
		</body>
        </f:view>
</html>



<#macro prepareEditTag isMandatory ColumnName resourceBundleTablePrefix ControlType dateFormat outputLabelColumnName>
<td>
<h:outputText value="${bean_prefix}msgs.${resourceBundleTablePrefix}_${outputLabelColumnName}}"/>
<#if ControlType=="date">
<h:outputText value="(${dateFormat})" style="color:red;"/>
</#if>
<#if isMandatory="true">
<h:outputText value="*"  style="color:red;" />
</#if>
</td>
</#macro>

<#macro prepareRadioTag isReadonly ColumnName beanClassName voInstanceName prefix LabelsRadio resourceBundleTablePrefix isMandatory ControlType dateFormat outputLabelColumnName>
<td>
<h:outputText value="${bean_prefix}msgs.${resourceBundleTablePrefix}_${outputLabelColumnName}}"/>
<#if ControlType=="date">
<h:outputText value="(${dateFormat})" style="color:red;"/>
</#if>
<#if isMandatory="true">
<h:outputText value="*"  style="color:red;" />
</#if>
</td>
<td>
<${prefix}:selectOneRadio id="${ColumnName}" <#if isReadonly="true"> ${EditReadOnlyTrue} </#if> value="${bean_prefix}${beanClassName}.${voInstanceName}.${ColumnName}}">
<#list LabelsRadio as radios>
<f:selectItem itemLabel="${radios.getKey()}" itemValue="${radios.getValue()}" />
</#list>
</${prefix}:selectOneRadio>
<@prepareMessage id=ColumnName prefix=prefix/>
</td>
</#macro>

<#macro prepareCheckBoxTag isReadonly ColumnName beanClassName voInstanceName prefix resourceBundleTablePrefix isMandatory ControlType dateFormat outputLabelColumnName>
<td>
<h:outputText value="${bean_prefix}msgs.${resourceBundleTablePrefix}_${outputLabelColumnName}}"/>
<#if ControlType=="date">
<h:outputText value="(${dateFormat})" style="color:red;"/>
</#if>
<#if isMandatory="true">
<h:outputText value="*"  style="color:red;" />
</#if>
</td>
<td>
<${prefix}:selectBooleanCheckbox <#if isReadonly=="true">readonly="true"</#if> id="${ColumnName}" value="${bean_prefix}${beanClassName}.${voInstanceName}.${ColumnName}}"/>
<@prepareMessage id=ColumnName prefix=prefix/>
</td>
</#macro>



<#macro TextAreaTag isReadonly cols ColumnName beanClassName voInstanceName prefix resourceBundleTablePrefix isMandatory ControlType dateFormat outputLabelColumnName>
<td>
<h:outputText value="${bean_prefix}msgs.${resourceBundleTablePrefix}_${outputLabelColumnName}}"/>
<#if ControlType=="date">
<h:outputText value="(${dateFormat})" style="color:red;"/>
</#if>
<#if isMandatory="true">
<h:outputText value="*"  style="color:red;" />
</#if>
</td>
<td>
<${prefix}:inputTextarea rows="4" cols="${cols}" <#if isReadonly=="true">${EditReadOnlyTrue}</#if> id="${ColumnName}" value="${bean_prefix}${beanClassName}.${voInstanceName}.${ColumnName}}"/>
<@prepareMessage id=ColumnName prefix=prefix/>
</td>
</#macro>

<#macro EditPageStaticSelectTag isReadonly ColumnName beanClassName voInstanceName resourceBundleTablePrefix isMandatory ControlType dateFormat outputLabelColumnName>
<td>
<h:outputText value="${bean_prefix}msgs.${resourceBundleTablePrefix}_${outputLabelColumnName}}"/>
<#if ControlType=="date">
<h:outputText value="(${dateFormat})" style="color:red;"/>
</#if>
<#if isMandatory="true">
<h:outputText value="*"  style="color:red;" />
</#if>
</td>
<td>
<h:selectOneMenu <#if isReadonly=="true">readonly="true"</#if> id="${ColumnName}" value="${bean_prefix}${beanClassName}.${voInstanceName}.${ColumnName}}">
<f:selectItems value="${bean_prefix}${beanClassName}.${ColumnName}}"/>
</h:selectOneMenu>
</td>
<@prepareMessage id=ColumnName prefix="h"/>
</td>
</#macro>

<#macro EditPageSelectTag isReadonly ColumnName beanClassName voInstanceName varName resourceBundleTablePrefix isMandatory ControlType dateFormat outputLabelColumnName>
<td>
<h:outputText value="${bean_prefix}msgs.${resourceBundleTablePrefix}_${outputLabelColumnName}}"/>
<#if ControlType=="date">
<h:outputText value="(${dateFormat})" style="color:red;"/>
</#if>
<#if isMandatory="true">
<h:outputText value="*"  style="color:red;" />
</#if>
</td>
<td>
<h:selectOneMenu <#if isReadonly=="true">readonly="true"</#if> id="${ColumnName}" value="${bean_prefix}${beanClassName}.${voInstanceName}.${ColumnName}}">
<f:selectItems value="${bean_prefix}${beanClassName}.${varName}}"/>
</h:selectOneMenu>
</td>
</#macro>


<#macro EditAuditingTag isReadonly ColumnName beanClassName voInstanceName prefix ControlType dateFormat resourceBundleTablePrefix isMandatory outputLabelColumnName>
<td>
<h:outputText value="${bean_prefix}msgs.${resourceBundleTablePrefix}_${outputLabelColumnName}}"/>
<#if ControlType=="date">
<h:outputText value="(${dateFormat})" style="color:red;"/>
</#if>
<#if isMandatory="true">
<h:outputText value="*"  style="color:red;" />
</#if>
</td>
<#if ControlType=="createddate">
<td>
<${prefix}:inputText value="${bean_prefix}${beanClassName}.${voInstanceName}.${ColumnName}}" style="background-color:#d3d3d3;" readonly="true">
<f:convertDateTime type="date" pattern="${dateFormat}"  timeZone="GMT+5.30"/>
</${prefix}:inputText>

<#elseif ControlType=="modifieddate">
<td>
<${prefix}:inputText value="${bean_prefix}${beanClassName}.${voInstanceName}.${ColumnName}}" style="background-color:#d3d3d3;" readonly="true">
<f:convertDateTime type="date" pattern="${dateFormat}" timeZone="GMT+5.30"/>
</${prefix}:inputText>

<#elseif ControlType=="modifiedby">
<td>
<${prefix}:inputText value="${bean_prefix}${beanClassName}.${voInstanceName}.${ColumnName}}" style="background-color:#d3d3d3;" readonly="true"/>

<#elseif ControlType=="createdby">
<td>
<${prefix}:inputText value="${bean_prefix}${beanClassName}.${voInstanceName}.${ColumnName}}" style="background-color:#d3d3d3;" readonly="true"/>
</#if>
</td>
</#macro> 


<#macro primeTag isReadonly isDate pk ColumnName  ControlType dateFormat beanClassName voInstanceName nullable dataLength validation minDateFormat  prefix isMandatory resourceBundleTablePrefix outputLabelColumnName fromDate= "" toDate="" >
<td>
<h:outputText value="${bean_prefix}msgs.${resourceBundleTablePrefix}_${outputLabelColumnName}}"/>
<#if ControlType=="date">
<h:outputText value="(${dateFormat})" style="color:red;"/>
</#if>
<#if isMandatory="true">
<h:outputText value="*"  style="color:red;" />
</#if>
</td>
<td>
<#if ControlType=="password">
<${prefix}:password
<#elseif ControlType=="date">
<p:calendar
<#else>
<${prefix}:inputText
</#if>
<#if pk==ColumnName>${Disabled}</#if>
<#if pk!=ColumnName>
	<#if nullable==0>
			<#if ControlType=="date">
				<#if isReadonly=="true">
			${NotNull}
			<#else>
				${NotNullandReadonlyfalse}
			</#if>
		<#else>
			<#if isReadonly=="true">
				${NotNullReadonlyTrueNoDate}
			<#else>	
				${NotNullReadonlyfalseNoDate}
			</#if>
		</#if>
	<#elseif ControlType=="date">
		<#if isReadonly=="true">
			${NullReadonlyTrueDate}
		<#else>	
			${NullReadonlyFalseDate}
		</#if>
	<#else>
		<#if isReadonly=="true">
			${NullReadonlyTrueNoDate}
		<#else>	
			${NullReadonlyFalseNoDate}
		</#if>
	</#if>	
</#if>
size="<#if ControlType=="date" && validation=="No Validation">${dataLength}" maxlength="${dataLength}" id="${ColumnName}" pattern="${dateFormat}" 		value="${bean_prefix}${beanClassName}.${voInstanceName}.${ColumnName}}" showOn="button"
	<#elseif ControlType=="date" && validation=="Future Date Validation">
	${dataLength}" maxlength="${dataLength}" mindate="${minDateFormat}" id="${ColumnName}" pattern="${dateFormat}" 		value="${bean_prefix}${beanClassName}.${voInstanceName}.${ColumnName}}" showOn="button"
	<#elseif ControlType=="date" && validation=="Date Range Validation">
	${dataLength}" maxlength="${dataLength}" mindate="${fromDate}" maxdate="${toDate}" id="${ColumnName}" pattern="${dateFormat}" 		value="${bean_prefix}${beanClassName}.${voInstanceName}.${ColumnName}}" showOn="button"
	<#else>	
" id="${ColumnName}" value="${bean_prefix}${beanClassName}.${voInstanceName}.${ColumnName}}"
</#if>
<#if ControlType=="date" && isReadonly=="true" && nullable==0>
	 > </p:inputText>
	<#elseif ControlType=="date" && isReadonly=="false" && nullable==0>
		><f:convertDateTime type="date" pattern="${dateFormat}" />
		</p:calendar>
	<#elseif ControlType=="date" && isReadonly=="true" && nullable!=0>
			> </p:inputText>
	<#elseif ControlType=="date" && isReadonly=="false" && nullable!=0>
		>
		<f:convertDateTime type="date" pattern="${dateFormat}" />
		</p:calendar>
	<#else>
	/>
</#if>	
</td>
	
<#if ControlType=="password" && (isReadonly=="false" || (isReadonly=="true" && nullable==0))>
	</tr>
		<tr>
			<td>
				Confirm <h:outputText value="${outputLabelColumnName}"/>
				<#if isMandatory=="true"><h:outputText value="*" style="color:red;" /></#if>
			</td>
			<td>
				<${prefix}:password <#if isReadonly=="false">${passwordReadOnlyfalse}<#elseif nullable==0>${passwordReadOnlytrue}></#if>  id="${ColumnName}password" value="${bean_prefix}${beanClassName}.${voInstanceName}.${ColumnName}}"/>
			</td>
</#if>	

</#macro>




<#macro DateValidationCheck ControlType validation dataLength ColumnName dateFormat beanClassName voInstanceName ColumnName minDateFormat fromDate toDate outputLabelColumnName>
size="
<#if ControlType=="date" && validation=="No Validation">
	${dataLength}" maxlength="${dataLength}" id="${ColumnName}" pattern="${dateFormat}" 		value="${bean_prefix}${beanClassName}.${voInstanceName}.${ColumnName}}" showOn="button"

	<#elseif ControlType=="date" && validation=="Future Date Validation">
	${dataLength}" maxlength="${dataLength}" mindate="${minDateFormat}" id="${ColumnName}" pattern="${dateFormat}" 		value="${bean_prefix}${beanClassName}.${voInstanceName}.${ColumnName}}" showOn="button"

	<#elseif ControlType=="date" && validation=="Date Range Validation">
	${dataLength}" maxlength="${dataLength}" mindate="${fromDate}" maxdate="${toDate}" id="${ColumnName}" pattern="${dateFormat}" 		value="${bean_prefix}${beanClassName}.${voInstanceName}.${ColumnName}}" showOn="button"

	<#else>	
${dataLength}" maxlength="${dataLength}" id="${ColumnName}" pattern="${dateFormat}" 		value="${bean_prefix}${beanClassName}.${voInstanceName}.${ColumnName}}" showOn="button"
</#if>

</#macro>		

<#macro DateReadonlyNullable isDate isReadonly nullable dateFormat outputLabelColumnName>
<#if isDate=="true" && isReadonly=="true" && nullable=="true">
	 > </p:inputText>
	<#elseif isDate=="true" && isReadonly=="false" && nullable=="true">
	><f:convertDateTime type="date" pattern="${dateFormat}" />
	</p:calendar>
	<#elseif isDate=="true" && isReadonly=="true" && nullable=="false">
	> </p:inputText>
	<#elseif isDate=="true" && isReadonly=="false" && nullable=="false">
	 >
	 <f:convertDateTime type="date" pattern="${dateFormat}" />
	 </p:calendar>
	<#else>
	/>
</#if>	
</#macro>	

<#macro PasswordandReadOnlyorReadonlyandNullable ControlType ColumnName prefix dataLength beanClassName voInstanceName  isMandatory isReadonly nullable outputLabelColumnName>
<#if ControlType=="password" && isReadonly=="false" || isReadonly=="true" && nullable=="0">
	</tr>
	<tr>
	<td>
	Confirm <h:outputText value="${ColumnName}"/>
	<#if isMandatory=="true"><h:outputText value="*" style="color:red;" /></#if>
	</td>
	<td>
	<prefix:password <#if isReadonly=="false">${passwordReadOnlyfalse}<#elseif nullable=="0">${passwordReadOnlytrue}></#if> id="${ColumnName}password" value="${bean_prefix}${beanClassName}.${voInstanceName}.${ColumnName}}"/>
	</td>
</#if>						
</#macro>


<#macro prepareMessage id prefix>
<${prefix}:message for="${id}" />
</#macro>

<#macro JavascriptValidator formName columnName ControlType LabelName>
if ( document.forms["${formName}Form"].elements["${formName}Form:${columnName}"].value.length == 0 )
{
alert('Please enter the value of ${LabelName});
document.forms["${formName}Form"].elements["${formName}Form:${columnName}"].focus();
return false;
}
<#if ControlType=="password">
if (document.forms["${formName}Form"].elements["${formName}Form:${columnName}password"].readOnly == false) {
if(!(document.forms["${formName}Form"].elements["${formName}Form:${columnName}"].value == document.forms["${formName}Form"].elements["${formName}Form:${columnName}password"].value ) )
{
alert('${columnName}does not match!!!);
document.forms["${formName}Form"].elements["${formName}Form:${columnName}"].focus();
return false;
}
}
</#if>
</#macro>