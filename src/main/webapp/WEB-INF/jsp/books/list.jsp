<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Library System | Book Catalog</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <nav>
        <div class="container">
            <div style="font-size: 1.5rem; font-weight: bold; color: var(--primary);">LibSystem</div>
            <div>
                <a href="/">Home</a>
                <a href="/books">Books</a>
                <a href="/users">Users</a>
                <a href="/transactions">Borrowings</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
            <h1>Book Catalog</h1>
            <a href="/books/add" class="btn btn-primary">+ Add New Book</a>
        </div>

        <div class="card">
            <table>
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Author</th>
                        <th>Type</th>
                        <th>ISBN</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="book" items="${books}">
                        <tr>
                            <td><strong>${book.title}</strong></td>
                            <td>${book.author}</td>
                            <td>
                                <span class="badge ${book.bookType == 'EBOOK' ? 'badge-warning' : 'badge-success'}">
                                    ${book.bookType}
                                </span>
                            </td>
                            <td>${book.isbn}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${book.available}">
                                        <span style="color: #166534;">Available</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: #991b1b;">Borrowed</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="/books/delete/${book.id}" style="color: #ef4444;" onclick="return confirm('Delete this book?')">Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
//home page 