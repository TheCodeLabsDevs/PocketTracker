<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>

<#macro form name url method="post" id=name rawUrl=false multipart=false classes="" csrf=true>
    <form name="${name}" id="${id}" class="${classes}"
          action="<#if rawUrl>${url}<#else><@s.url url/></#if>" method="${method}"
          <#if multipart>enctype="multipart/form-data"</#if>
    >
        <#if _csrf?? && csrf>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </#if>

        <#nested>
    </form>
</#macro>

<#macro submit label="button.save" form="" size="col-12" col=true classes="" style="btn-primary" icon="">
    <#if col><div class="mb-3 ${size}"></#if>
    <button type="submit" <#if form?has_content>form="${form}"</#if> class="btn ${style} ${classes}">
        <#if icon?has_content><i class="${icon}"></i></#if>
        <@b.localize label/>
    </button>
    <#if col></div></#if>
</#macro>

<#macro input label name value="" type="text" id=name size="col-6">
    <div class="mb-3 ${size}">
        <label for="${id}" class="form-label"><@b.localize label/></label>
        <input type="${type}" class="form-control <#if hasError(name)>is-invalid</#if>" id="${id}" name="${name}" value="${value}">
        <@inputError fieldName=name/>
    </div>
</#macro>

<#macro textarea label name value="" type="text" id=name size="col-6">
    <div class="mb-3 ${size}">
        <label for="${id}" class="form-label"><@b.localize label/></label>
        <textarea class="form-control <#if hasError(name)>is-invalid</#if>" id="${id}" name="${name}" rows="4">${value}</textarea>
        <@inputError fieldName=name/>
    </div>
</#macro>

<#macro select name options value="" label="" id=name size="col-6" classes="">
    <div class="mb-3 ${size} ${classes}">
        <#if label?has_content>
            <label for="${id}" class="form-label"><@b.localize label/></label>
        </#if>
        <select class="form-select <#if hasError(name)>is-invalid</#if>" id="${id}" name="${name}">
            <#list options as option>
                <option <#if value == option>selected</#if> value="${option}"><@b.localize option/></option>
            </#list>
        </select>
        <@inputError fieldName=name/>
    </div>
</#macro>

<#macro switch label name id=name size="col-6" value=false>
    <div class="mb-3 ${size}">
        <div class="form-check form-switch">
            <input class="form-check-input" type="checkbox" id="${id}" name="${name}" <#if value>checked</#if>>
            <label class="form-check-label" for="${id}"><@b.localize label/></label>
        </div>
    </div>
</#macro>

<#macro file label name id=name size="col-12" accept="">
    <div class="mb-3 ${size}">
        <label for="${id}" class="form-label"><@b.localize label/></label>
        <input class="form-control" type="file" id="${id}" name="${name}" <#if accept?has_content>accept="${accept}"</#if>>
    </div>
</#macro>

<#function hasError fieldName>
    <#if validation?? && validation.hasFieldErrors(fieldName)>
        <#return true>
    </#if>
    <#return false/>
</#function>

<#macro inputError fieldName>
    <#if hasError(fieldName)>
        <div class="invalid-feedback">
            <@b.localize validation.getFieldError(fieldName)/>
        </div>
    </#if>
</#macro>