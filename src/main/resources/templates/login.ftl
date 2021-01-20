<!doctype html>
<html lang="en">
    <head>
        <#import "header.ftl" as headerMacros>
        <@headerMacros.header 'Login'/>

        <link href="<@s.url "/css/login.css"/>" rel="stylesheet">
    </head>

    <body class="bg-light">
        <#import "navbar.ftl" as navbar>
        <@navbar.navbar/>

        <main>
            <div class="form-signin align-middle card">
                <form>
                    <img class="mb-4" src="<@s.url "/image/PocketTracker.png"/>" alt="" width="80" height="80">
                    <h1 class="h3 mb-3 fw-normal">PocketTracker</h1>
                    <label for="inputUserName" class="visually-hidden">Username</label>
                    <input type="text" id="inputUserName" class="form-control" placeholder="Username" required autofocus>
                    <label for="inputPassword" class="visually-hidden">Password</label>
                    <input type="password" id="inputPassword" class="form-control" placeholder="Password" required>
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
