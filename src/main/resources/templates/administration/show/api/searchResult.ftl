<#import "/common/components/base.ftl" as b>
<#import "/common/components/form.ftl" as f>

<#setting number_format="computer">

<table class="table" id="searchResultTable">
    <tr>
        <th><@b.localize "show.apiIdentifiers.search.name"/></th>
        <th><@b.localize "show.apiIdentifiers.search.firstAired"/></th>
        <th><@b.localize "show.apiIdentifiers.search.identifier"/></th>
        <th></th>
    </tr>
    <#list items as item>
        <tr>
            <td>${item.name}</td>
            <td>${item.firstAired}</td>
            <td>${item.identifier}</td>
            <td>
                <@f.form name="search-${item.identifier}" url="${targetUrl}">
                    <@f.hidden id="type" value="${type}"/>
                    <@f.hidden id="identifier" value="${item.identifier}"/>
                    <@f.submit label="button.add" form="search-${item.identifier}" col=false/>
                </@f.form>
            </td>
        </tr>
    </#list>
</table>