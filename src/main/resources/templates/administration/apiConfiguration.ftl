<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/components/form.ftl" as f/>
    <#import "/common/components/table.ftl" as t/>
    <#import '/common/components/card.ftl' as c>
    <#import "/common/components/modal.ftl" as m/>
    <#import "/common/macros/deleteModal.ftl" as delete>

    <@template.head 'APIs'/>
    <@template.body>
        <@b.row>
            <@b.col "col-12 mb-3">
                <@b.h2 "menu.administration.apis"/>
            </@b.col>
        </@b.row>

        <@b.h3 title="api.config.create"/>
        <@showApiConfigurationForm config=newConfiguration url="/administration/apiConfiguration/create" includeDeleteButton=false formName="newApiConfiguration"/>

        <div class="mt-5">
            <@b.h3 title="api.config.configurations"/>
        </div>
        <@b.row>
            <#list apiConfigurations as config>
                <@showApiConfigurationForm config=config url="/administration/apiConfiguration/${config.id?c}/edit"/>
            </#list>
        </@b.row>
    </@template.body>
</html>

<#macro showApiConfigurationForm config url includeDeleteButton=true formName="apiConfiguration">
    <@c.card classes="my-4">
        <@c.body>
            <@b.row>
                <@f.form name=formName url=url rawUrl=true>
                    <@b.row>
                        <@f.select objectName=formName label="show.type" name="type" options=apiConfigurationTypes value=config.type/>
                        <@f.input objectName=formName label="api.config.token" name="token" value=config.token!""/>

                        <div class="d-flex justify-content-end">
                            <@f.submit size=""/>
                            <#if includeDeleteButton>
                                <#assign modalId = "delete-api-configuration-${config.id?c}">
                                <@m.open label="api.config.delete" modalId=modalId buttonSize="btn" style="btn-danger ms-3"/>
                            </#if>
                        </div>
                    </@b.row>
                </@f.form>

                <#if includeDeleteButton>
                    <@delete.modal modalId=modalId title="api.config.delete" deleteButton="api.config.delete" url="/administration/apiConfiguration/${config.id?c}/delete">
                        <@s.messageArgs code="api.config.delete.message" args=[config.getType().name()]/>
                    </@delete.modal>
                </#if>
            </@b.row>
        </@c.body>
    </@c.card>
</#macro>
