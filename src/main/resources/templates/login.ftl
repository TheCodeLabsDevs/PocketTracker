<!doctype html>
<html lang="en">
    <#import "/spring.ftl" as s/>
    <#import "/common/helpers.ftl" as helperMacros>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/users/form.ftl" as form>

    <@template.head 'Episodes'>
        <link href="<@s.url "/css/login.css"/>" rel="stylesheet">
    </@template.head>

    <@template.body container=false>
        <div class="form-signin align-middle card">
            <form method="post" action="<@s.url "/login"/>" role="form">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <img class="mb-4" src="<@s.url "/image/PocketTracker.png"/>" alt="" width="80" height="80">
                <h1 class="h3 mb-3 fw-normal">PocketTracker</h1>

                <label for="username" class="visually-hidden">Username</label>
                <input type="text" id="username" name="username" class="form-control" placeholder="Username" required
                       autofocus>

                <label for="password" class="visually-hidden">Password</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="Password"
                       required>

                <#if springMacroRequestContext.getQueryString()?? && springMacroRequestContext.getQueryString() == "error">
                    <div class="mt-4 mb-4 alert alert-danger" role="alert">
                        Falsches Passwort
                    </div>
                </#if>

                <div class="checkbox mb-3">
                    <label>
                        <input type="checkbox" name="remember-me" id="remember-me"> Remember me
                    </label>
                </div>
                <button class="w-100 mb-3 btn btn-lg btn-primary" type="submit">Login</button>
                <@b.button id="oauth-login-thecodelabs" classes="w-100 btn-lg" url="/oauth2/authorization/gitlab" icon="fab fa-gitlab" label="TheCodeLabs"/>
            </form>
        </div>
    </@template.body>
</html>
