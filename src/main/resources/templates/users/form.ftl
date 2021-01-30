<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
<#import "/common/components/form.ftl" as f/>
<#import "/common/components/card.ftl" as c/>

<#macro form user authentications>
    <@f.form name="user" url=springMacroRequestContext.getRequestUri() rawUrl=true>
        <@c.card classes="my-4">
            <@c.body>
                <@b.h4 "user.form.provider"/>
                <ul>
                    <#list authentications as authentication>
                        <li><@b.localize authentication/></li>
                    </#list>
                </ul>
            </@c.body>
        </@c.card>

        <@c.card classes="my-4">
            <@c.body>
                <@b.row>
                    <@b.h4 "authentication.provider.internal"/>
                    <@f.input label="user.form.username" name="username" value=user.username size="col-12"/>
                    <@f.input label="user.form.password" name="password" type="password"/>
                    <@f.input label="user.form.passwordRepeat" name="passwordRepeat" type="password"/>

                    <@f.submit "button.save"/>
                </@b.row>
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
    </@f.form>
</#macro>