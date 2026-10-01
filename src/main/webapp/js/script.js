document.addEventListener('DOMContentLoaded', function () {
    var input = document.getElementById('searchInput');
    var button = document.getElementById('searchBtn');
    var resultsEl = document.getElementById('results');
    var statusEl = document.getElementById('status');

    function search() {
        var query = input.value.trim();

        if (query === '') {
            statusEl.textContent = 'Please enter a title or author to search.';
            resultsEl.innerHTML = '';
            return;
        }

        statusEl.textContent = 'Searching...';
        resultsEl.innerHTML = '';

        fetch('search?query=' + encodeURIComponent(query))
            .then(function (response) {
                if (!response.ok) {
                    throw new Error('Server error: ' + response.status);
                }
                return response.json();
            })
            .then(function (books) {
                renderResults(books, query);
            })
            .catch(function (error) {
                statusEl.textContent = 'Something went wrong. Please try again.';
                console.error(error);
            });
    }

    function renderResults(books, query) {
        resultsEl.innerHTML = '';

        if (books.length === 0) {
            statusEl.textContent = 'No books found for "' + query + '".';
            return;
        }

        statusEl.textContent = books.length + ' result(s) for "' + query + '"';

        books.forEach(function (book) {
            var card = document.createElement('div');
            card.className = 'book-card';
            card.innerHTML =
                '<div class="book-title">' + escapeHtml(book.title) + '</div>' +
                '<div class="book-author">by ' + escapeHtml(book.author) + '</div>' +
                '<div class="book-meta">' +
                    '<span class="book-genre">' + escapeHtml(book.genre) + '</span>' +
                    '<span class="book-year">' + book.year + '</span>' +
                '</div>';
            resultsEl.appendChild(card);
        });
    }

    function escapeHtml(str) {
        var div = document.createElement('div');
        div.textContent = str;
        return div.innerHTML;
    }

    button.addEventListener('click', search);
    input.addEventListener('keydown', function (e) {
        if (e.key === 'Enter') {
            search();
        }
    });
});
