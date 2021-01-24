<#import "/spring.ftl" as s/>

<#macro form name url method="post" id=name rawUrl=false>
    <form name="${name}" id="${id}" action="<#if rawUrl>${url}<#else><@s.url url/></#if>" method="${method}">
        <#if _csrf??>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </#if>

        <#nested>
    </form>
</#macro>

<#macro submit label size="col-6" col=true>
    <#if col><div class="mb-3 ${size}"></#if>
        <button type="submit" class="btn btn-primary">${label}</button>
    <#if col></div></#if>
</#macro>

<#macro input label name value="" type="text" id=name size="col-6">
    <div class="mb-3 ${size}">
        <label for="${id}" class="form-label">${label}</label>
        <input type="${type}" class="form-control" id="${id}" name="${name}" value="${value}">
    </div>
</#macro>

<#macro select label name options value="" id=name size="col-6">
    <div class="mb-3 ${size}">
        <label for="${id}" class="form-label">${label}</label>
        <select class="form-select" id="${id}" name="${name}">
            <#list options as option>
                <option <#if value == option>selected</#if> value="${option}">${option}</option>
            </#list>
        </select>
    </div>
</#macro>