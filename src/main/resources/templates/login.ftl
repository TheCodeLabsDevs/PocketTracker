<!doctype html>
<html lang="en">
    <#import "/spring.ftl" as s/>
    <#import "/common/helpers.ftl" as helperMacros>

    <#import "/common/head.ftl" as headMacros>
    <@headMacros.head 'Episodes'>
        <link href="<@s.url "/css/login.css"/>" rel="stylesheet">
    </@headMacros.head>

    <body class="bg-light">
        <#import "/common/navbar.ftl" as navbar>
        <@navbar.navbar/>

        <main>
            <div class="form-signin align-middle card">
                <form method="post" action="<@s.url "/login"/>" role="form">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <img class="mb-4" src="<@s.url "/image/PocketTracker.png"/>" alt="" width="80" height="80">
                    <h1 class="h3 mb-3 fw-normal">PocketTracker</h1>

                    <label for="username" class="visually-hidden">Username</label>
                    <input type="text" id="username" name="username" class="form-control" placeholder="Username" required
                           autofocus>

                    <label for="password" class="visually-hidden">Password</label>
                    <input type="password" id="password" name="password" class="form-control" placeholder="Password" required>

                    <div class="checkbox mb-3">
                        <label>
                            <input type="checkbox" value="remember-me"> Remember me
                        </label>
                    </div>
                    <button class="w-100 btn btn-lg btn-primary" type="submit">Login</button>
                </form>
            </div>
        </main>
    </body>
</html>
