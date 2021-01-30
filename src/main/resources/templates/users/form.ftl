<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
<#import "/common/components/form.ftl" as f/>
<#import "/common/components/table.ftl" as t/>
<#import "/common/components/card.ftl" as c/>

<#macro form user authentications isGitlabConnected>
    <@c.card classes="my-4">
        <@c.body>
            <@b.h4 "authentication.provider.headline"/>
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
                                <button type="button" class="btn btn-sm btn-danger" data-bs-toggle="modal" data-bs-target="#provider-logout-${authentication.id}">
                                    <@b.localize "button.logout"/>
                                </button>

                                <div class="modal fade" id="provider-logout-${authentication.id}" tabindex="-1">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="exampleModalLabel"><@b.localize authentication/></h5>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                            </div>
                                            <div class="modal-body">
                                                <@b.localize "authentication.provider.logout"/>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><@b.localize "button.cancel"/></button>
                                                <@f.form name="provider-${authentication.id}" url="/user/settings/provider/${authentication.id}/delete">
                                                    <@f.submit label="button.logout" form="provider-${authentication.id}" col=false/>
                                                </@f.form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </#if>
                        </@t.cell>
                    </@t.row>
                </#list>
            </@t.table>
        </@c.body>
    </@c.card>

    <@c.card classes="my-4">
        <@c.body>
            <@f.form name="user" url=springMacroRequestContext.getRequestUri() rawUrl=true>
                <@b.row>
                    <@b.h4 "authentication.provider.internal"/>
                    <@f.input label="user.form.username" name="username" value=user.username size="col-12"/>
                    <@f.input label="user.form.password" name="password" type="password"/>
                    <@f.input label="user.form.passwordRepeat" name="passwordRepeat" type="password"/>

                    <@f.submit "button.save"/>
                </@b.row>
            </@f.form>
        </@c.body>
    </@c.card>

    <#if !isGitlabConnected && oauthEnabled>
        <@c.card classes="my-4">
            <@c.body>
                <@b.h4 "authentication.provider.gitlab"/>
                <@b.button label="user.form.connectWithGitlab" id="oauth-login-thecodelabs" classes="floating-end" url="/oauth2/authorization/gitlab" icon="fab fa-gitlab"/>
            </@c.body>
        </@c.card>
    </#if>
</#macro>