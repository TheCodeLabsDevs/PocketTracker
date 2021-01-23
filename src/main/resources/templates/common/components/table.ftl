<#import "/spring.ftl" as s/>

<#macro table id>
    <table id="${id}" class="table">
        <#nested>
    </table>
</#macro>

<#macro head>
    <thead>
        <tr>
            <#nested>
        </tr>
    </thead>
</#macro>

<#macro headCell label>
    <th scope="col">
        ${label}
    </th>
</#macro>

<#macro body>
    <tbody>
        <#nested>
    </tbody>
</#macro>

<#macro row>
    <tr>
        <#nested>
    </tr>
</#macro>

<#macro cell value="">
    <td><#if value?has_content>${value}<#else><#nested></#if></td>
</#macro>

<#macro action icon url="" onclick="" style="" id="" tooltip="" classes="" staticUri=false>
    <a
            <#if url?has_content>href="<#if !staticUri><@s.url url/><#else>${url}</#if>"</#if>
            <#if onclick?has_content>onclick="${onclick}"</#if>
            <#if id?has_content>id="${id}"</#if>
            class="px-2 table-action ${classes}"
            <#if tooltip?has_content>data-bs-toggle="tooltip" data-bs-placement="bottom" title="${tooltip}"</#if>
    >
        <i class="${icon} ${style}"></i>
    </a>
</#macro>