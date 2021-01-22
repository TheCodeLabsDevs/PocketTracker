<#import "/common/macros/base.ftl" as b/>
<#import "/common/macros/form.ftl" as f/>
<#import "/common/macros/card.ftl" as c/>

<#macro form user userTypes>
    <@f.form name="user" url="${springMacroRequestContext.contextPath}">
        <@c.card>
            <@c.body>
                <@b.row>
                    <@f.input label="Nutzername" name="username" value=user.username size="col-12"/>

                    <@f.input label="Passwort" name="password" type="password"/>
                    <@f.input label="Passwort wiederholen" name="passwordRepeat" type="password"/>

                    <@f.submit "Speichern"/>
                </@b.row>
            </@c.body>
        </@c.card>
    </@f.form>

</#macro>