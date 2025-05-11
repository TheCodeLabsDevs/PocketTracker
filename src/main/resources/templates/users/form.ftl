<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
<#import "/common/components/card.ftl" as c/>
<#import "/common/components/form.ftl" as f/>
<#import "/common/components/modal.ftl" as m/>
<#import "/common/components/table.ftl" as t/>

<#macro form user authentications isGitlabConnected>
    <@c.card classes="my-4">
        <@c.body>
            <h4><i class="fas fa-id-card"></i> <@b.localize "authentication.provider.headline"/></h4>
            <@t.table id="provider">
                <@t.head>
                    <@t.headCell "authentication.provider.table.provider"/>
                    <@t.headCell "authentication.provider.table.actions"/>
                </@t.head>
                <#list authentications as authentication>
                    <@t.row>
                        <@t.content authentication/>
                        <@t.cell>
                            <#if authentications?size gt 1>
                                <@m.open label="button.logout" modalId="provider-logout-${authentication.id}" style="btn-danger" icon="fas fa-arrow-right-from-bracket"/>

                                <@m.modal id="provider-logout-${authentication.id}">
                                    <@m.header authentication/>
                                    <@m.body>
                                        <@b.localize "authentication.provider.logout"/>
                                    </@m.body>
                                    <@m.footer>
                                        <@m.cancelButton/>
                                        <@f.form name="provider-${authentication.id}" url="/user/settings/provider/${authentication.id}/delete">
                                            <@f.submit label="button.logout" form="provider-${authentication.id}" style="btn-danger" col=false icon="fas fa-arrow-right-from-bracket"/>
                                        </@f.form>
                                    </@m.footer>
                                </@m.modal>
                            </#if>
                        </@t.cell>
                    </@t.row>
                </#list>
            </@t.table>
        </@c.body>
    </@c.card>

    <@c.card classes="my-4">
        <@c.body>
            <#assign objectName="user"/>
            <@f.form name=objectName url=springMacroRequestContext.getRequestUri() rawUrl=true>
                <@b.row>
                    <@b.h4 "authentication.provider.internal"/>
                    <@f.input objectName=objectName label="user.form.username" name="username" value=user.username size="col-12"/>
                    <@f.input objectName=objectName label="user.form.password" name="password" type="password"/>
                    <@f.input objectName=objectName label="user.form.passwordRepeat" name="passwordRepeat" type="password"/>

                    <@f.submit icon="fas fa-save"/>
                </@b.row>
            </@f.form>
        </@c.body>
    </@c.card>

    <#if !isGitlabConnected && oauthEnabled>
        <@c.card classes="my-4">
            <@c.body>
                <#assign objectName="oauth"/>
                <@f.form name=objectName url="/user/settings/oauth/gitlab">
                    <@b.row>
                        <h4><i class="fab fa-gitlab"></i> <@b.localize "authentication.provider.gitlab"/></h4>
                        <@f.input objectName=objectName label="user.form.username" name="username" size="col-12"/>

                        <@f.submit label="user.form.connectWithGitlab" icon="fab fa-gitlab"/>
                    </@b.row>
                </@f.form>
            </@c.body>
        </@c.card>
    </#if>
</#macro>