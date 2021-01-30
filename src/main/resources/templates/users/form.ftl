<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
<#import "/common/components/form.ftl" as f/>
<#import "/common/components/card.ftl" as c/>

<#macro form user authentications>
    <@f.form name="user" url=springMacroRequestContext.getRequestUri() rawUrl=true>
        <@c.card classes="my-4">
            <@c.body>
                <@b.h4 "Aktive Authentication Provider" true/>
                <ul>
                    <#list authentications as authentication>
                        <li><@b.localize "authentication.provider." + authentication/></li>
                    </#list>
                </ul>
            </@c.body>
        </@c.card>

        <@c.card classes="my-4">
            <@c.body>
                <@b.row>
                    <@b.h4 "authentication.provider.internal"/>
                    <@f.input label="Nutzername" name="username" value=user.username size="col-12"/>
                    <@f.input label="Passwort" name="password" type="password"/>
                    <@f.input label="Passwort wiederholen" name="passwordRepeat" type="password"/>

                    <@f.submit "Speichern"/>
                </@b.row>
            </@c.body>
        </@c.card>

        <#if !authentications?seq_contains("gitlab") && oauthEnabled>
            <@c.card classes="my-4">
                <@c.body>
                    <@b.h4 "authentication.provider.gitlab"/>
                    <@b.button id="oauth-login-thecodelabs" classes="floating-end" url="/oauth2/authorization/gitlab" icon="fab fa-gitlab" label="Connect with TheCodeLabs"/>
                </@c.body>
            </@c.card>
        </#if>
    </@f.form>
</#macro>