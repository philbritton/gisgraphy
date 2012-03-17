<html>
<head>
<title>
<#if !openstreetmap?? >
   <@s.text name="global.crud.create.new.street"/>
<#else>
<@s.text name="button.edit"/> <@s.text name="global.street"/>
'<#if openstreetmap.name??>${openstreetmap.name}<#else><@s.text name="global.street.noname" /></#if>'
</#if>
</title>
</head>
<body>
<div class="clear"><br/></div>
<h1>
<#if !openstreetmap?? >
   <@s.text name="global.crud.create.new.street"/>
<#else>
<@s.text name="button.edit"/> <@s.text name="global.street"/>
'<#if openstreetmap.name??>${openstreetmap.name}<#else><@s.text name="global.street.noname" /></#if>'
</#if>
</h1>
			<div class="clear"><br/></div>
	 			<#if openstreetmap?? >
	 			<@s.url id="addstreet" action="editStreet!input" includeParams="none" namespace="/admin" />
<img src="/images/add.png"/ alt="" style="padding-right:5px;vertical-align:middle;"/><a href="${addstreet}"><@s.text name="global.crud.create.new.street"/></a>

<@s.url id="deletestreet" action="editStreet!delete" includeParams="none" namespace="/admin" />
<span style="float:right;margin-right:130px;"><@s.form action="${deletestreet}" method="post" theme="simple" id="deleteForm">
  <@s.hidden name="gid" value="%{openstreetmap.gid}"/>
<img src="/images/delete.png"/ alt="" style="padding-right:5px;vertical-align:middle;"/><a href="#" onCLick="if (confirmDelete('street')){${'deleteForm'}.submit();}"><@s.text name="global.crud.delete.street"/></a>
</@s.form>
</span>
<br/><br/>
</#if>
<img src="/images/required_field.png"/ alt="" style="vertical-align:middle;"/> <@s.text name="search.required.label"/>
<br/>
<@s.url id="saveurl" action="editStreet" includeParams="none" method="save" namespace="/admin" />
<@s.form action="${saveurl}" method="post">
<fieldset>
<legend>

<#if !openstreetmap?? >
   <@s.text name="global.crud.create.new.street"/>
<#else>
<@s.text name="button.edit"/> <@s.text name="global.street"/> 
'<#if openstreetmap.name??>${openstreetmap.name}<#else><@s.text name="global.street.noname" /></#if>'
</#if>
</legend>
<@s.hidden name="gid" value="%{openstreetmap.gid}"/>
  <#if openstreetmap?? && openstreetmap.gid??>
	<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="global.FeatureId"/> : </span>${openstreetmap.gid?c}
		</span>
		<div class="clear"></div>
</#if>

<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="global.name"/> : </span><@s.textfield name="openstreetmap.name" value="%{openstreetmap.name}" theme="simple" size="35"/>
		</span>
		<div class="clear"></div>

  <span class="searchfield">
			<span class="searchfieldlabel"><img src="/images/required_field.png"/ alt="" style="vertical-align:middle;"/><@s.text name="global.latitude"/> : </span>  <@s.textfield name="latitude" value="%{openstreetmap.location.y}" theme="simple" size="35" required="true"/>
  </span>
  <div class="clear"></div>

<span class="searchfield">
			<span class="searchfieldlabel"><img src="/images/required_field.png"/ alt="" style="vertical-align:middle;"/><@s.text name="global.longitude"/> : </span><@s.textfield name="longitude" value="%{openstreetmap.location.x}" theme="simple" size="35" required="true"/>
		</span>
		<div class="clear"></div>

<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="global.shape"/> : </span><@s.textfield name="shape" value="%{openstreetmap.shape}" theme="simple" size="35"/>
		</span>
		<div class="clear"></div>
<br/><@s.text name="shape.format"/><br/><br/>


<#if openstreetmap?? && openstreetmap.length??>
<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="global.length"/> (<@s.text name="global.unit"/>) : </span> ${openstreetmap.length}
</span>
		<div class="clear"></div>
</#if>

<span class="searchfield">
			<span class="searchfieldlabel"><img src="/images/required_field.png"/ alt="" style="vertical-align:middle;"/><@s.text name="global.country"/> : </span><@s.select label="countries" listKey="iso3166Alpha2Code" listValue="name" name="openstreetmap.countryCode" list="countries" headerValue="--choose--" headerKey="" multiple="false" required="true" labelposition="left" theme="simple" /> 
		</span>
		<div class="clear"></div>
<br/>


<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="street.oneway"/> : </span><@s.select name="openstreetmap.oneWay" list="%{#@java.util.LinkedHashMap@{'true' : getText('global.yes'), 'false': getText('global.no')}}" value="%{openstreetmap.oneWay}" theme="simple"/>
		</span><br/><br/>
		<div class="clear"></div>



<div class="clear"></div>
		<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="global.streettype"/> : </span>
			<@s.select headerKey="" headerValue="--Choose--"  name="streettype" list="streetTypes"  multiple="false" theme="simple" value="%{openstreetmap.streetType}" />
			<br/>
		</span>
		<br/>


<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="global.is.in"/> : </span><@s.textfield name="openstreetmap.isIn" value="%{openstreetmap.isIn}" theme="simple" size="35"/>
		</span>
		<div class="clear"></div>
<#if !openstreetmap?? >
<@s.text name="gid.autogenerated"/>
</#if>
<br/>
<br/>
<div style="float:right;">
<@s.url id="cancelurl" action="editSearch" includeParams="none" namespace="/admin" />
 <@s.submit value="%{getText('button.save')}"  theme="simple"/> <input type="button" value="<@s.text name="button.cancel"/>" onClick="document.location.href='${cancelurl}'" />
<br/><br/>
</div>
</fieldset>
</@s.form>
<br/><br/>
<br/>
</body>
</html>