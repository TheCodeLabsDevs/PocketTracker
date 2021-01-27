<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
<#import "/common/components/form.ftl" as f/>
<#import "/common/components/card.ftl" as c/>

<#macro form user authentications>
    <@f.form name="user" url=springMacroRequestContext.getRequestUri() rawUrl=true>
        <@c.card>
            <@c.body>
                <@b.row>
                    <@b.h4 "Aktive Authentication Provider" true/>
                    <@b.col>
                        <ul>
                            <#list authentications as authentication>
                                <li><@s.message "authentication.provider." + authentication/></li>
                            </#list>
                        </ul>
                    </@b.col>
                    <@b.separator/>

                    <@b.h4 "authentication.provider.internal"/>
                    <@f.input label="Nutzername" name="username" value=user.username size="col-12"/>
                    <@f.input label="Passwort" name="password" type="password"/>
                    <@f.input label="Passwort wiederholen" name="passwordRepeat" type="password"/>

                    <@f.submit "Speichern"/>

                    <#if !authentications?seq_contains("gitlab") && oauthEnabled>
                        <@b.separator/>

                        <@b.h4 "authentication.provider.gitlab"/>
                        <@b.col>
                            <@b.button id="oauth-login-thecodelabs" classes="floating-end" url="/oauth2/authorization/gitlab" icon="fab fa-gitlab" label="Connect with TheCodeLabs"/>
                        </@b.col>
                    </#if>
                </@b.row>
            </@c.body>
        </@c.card>
    </@f.form>

</#macro>