<#import "/common/components/base.ftl" as b/>
<#import "/common/components/form.ftl" as f/>
<#import "/common/components/card.ftl" as c/>

<#macro form user authentications>
    <@f.form name="user" url="${springMacroRequestContext.getRequestUri()}">
        <@c.card>
            <@c.body>
                <@b.row>
                    <@f.input label="Nutzername" name="username" value=user.username size="col-12"/>

                    <@f.input label="Passwort" name="password" type="password"/>
                    <@f.input label="Passwort wiederholen" name="passwordRepeat" type="password"/>

                    <@b.h4 "Authentication Provider"/>
                    <@b.col>
                        <ul>
                            <#list authentications as authentication>
                                <li>${authentication}</li>
                            </#list>
                        </ul>
                    </@b.col>

                    <@f.submit "Speichern"/>

                    <#if !authentications?seq_contains("GitlabAuthentication") && oauthEnabled>
                        <@b.button id="oauth-login-thecodelabs" classes="floating-end" url="/oauth2/authorization/gitlab" icon="fab fa-gitlab" label="Connect with TheCodeLabs"/>
                    </#if>
                </@b.row>
            </@c.body>
        </@c.card>
    </@f.form>

</#macro>