// HealthSure App JavaScript

document.addEventListener('DOMContentLoaded', function() {
    initSidebar();
    initAlertDismiss();
    initFormValidation();
    initFileUpload();
    initConfirmDialogs();
    initTooltips();
});

// ===== Sidebar Mobile Toggle =====
function initSidebar() {
    const sidebar = document.querySelector('.sidebar');
    const toggleBtn = document.getElementById('sidebar-toggle');
    if (toggleBtn && sidebar) {
        toggleBtn.addEventListener('click', function() {
            sidebar.classList.toggle('open');
        });
        document.addEventListener('click', function(e) {
            if (sidebar.classList.contains('open')
                && !sidebar.contains(e.target)
                && e.target !== toggleBtn) {
                sidebar.classList.remove('open');
            }
        });
    }

    // Highlight active nav link
    const currentPath = window.location.pathname;
    document.querySelectorAll('.sidebar-nav .nav-link').forEach(link => {
        const href = link.getAttribute('href');
        if (href && currentPath.startsWith(href) && href !== '/') {
            link.classList.add('active');
        } else if (href === '/' && currentPath === '/') {
            link.classList.add('active');
        }
    });
}

// ===== Auto-dismiss alerts =====
function initAlertDismiss() {
    document.querySelectorAll('.alert-dismissible').forEach(alert => {
        const closeBtn = alert.querySelector('[data-dismiss]');
        if (closeBtn) {
            closeBtn.addEventListener('click', () => {
                alert.style.opacity = '0';
                setTimeout(() => alert.remove(), 300);
            });
        }
        // Auto-dismiss after 5 seconds
        setTimeout(() => {
            if (alert.parentElement) {
                alert.style.transition = 'opacity 0.5s';
                alert.style.opacity = '0';
                setTimeout(() => alert.remove(), 500);
            }
        }, 5000);
    });
}

// ===== Client-side form validation hints =====
function initFormValidation() {
    const forms = document.querySelectorAll('form[data-validate]');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            const requiredFields = form.querySelectorAll('[required]');
            let valid = true;
            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    field.classList.add('is-invalid');
                    valid = false;
                } else {
                    field.classList.remove('is-invalid');
                }
            });
            if (!valid) {
                e.preventDefault();
                form.querySelector('.is-invalid')?.focus();
            }
        });

        form.querySelectorAll('[required]').forEach(field => {
            field.addEventListener('input', () => {
                if (field.value.trim()) {
                    field.classList.remove('is-invalid');
                }
            });
        });
    });
}

// ===== File Upload Zone =====
function initFileUpload() {
    const uploadZone = document.getElementById('upload-zone');
    const fileInput = document.getElementById('file-input');
    const fileLabel = document.getElementById('file-label');

    if (!uploadZone || !fileInput) return;

    uploadZone.addEventListener('click', () => fileInput.click());

    uploadZone.addEventListener('dragover', (e) => {
        e.preventDefault();
        uploadZone.classList.add('drag-over');
    });

    uploadZone.addEventListener('dragleave', () => {
        uploadZone.classList.remove('drag-over');
    });

    uploadZone.addEventListener('drop', (e) => {
        e.preventDefault();
        uploadZone.classList.remove('drag-over');
        const files = e.dataTransfer.files;
        if (files.length > 0) {
            fileInput.files = files;
            updateFileLabel(files[0].name, files[0].size);
        }
    });

    fileInput.addEventListener('change', () => {
        if (fileInput.files.length > 0) {
            updateFileLabel(fileInput.files[0].name, fileInput.files[0].size);
        }
    });

    function updateFileLabel(name, size) {
        if (fileLabel) {
            const sizeMB = (size / 1048576).toFixed(2);
            fileLabel.textContent = `${name} (${sizeMB} MB)`;
            fileLabel.style.color = 'var(--primary)';
            fileLabel.style.fontWeight = '600';
        }
    }
}

// ===== Confirm dialogs =====
function initConfirmDialogs() {
    document.querySelectorAll('[data-confirm]').forEach(el => {
        el.addEventListener('click', function(e) {
            const msg = el.dataset.confirm || 'Are you sure?';
            if (!confirm(msg)) {
                e.preventDefault();
                return false;
            }
        });
    });
}

// ===== Simple tooltips =====
function initTooltips() {
    document.querySelectorAll('[data-tooltip]').forEach(el => {
        el.setAttribute('title', el.dataset.tooltip);
    });
}

// ===== Format currency =====
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-IN', {
        style: 'currency',
        currency: 'INR',
        maximumFractionDigits: 2
    }).format(amount);
}

// ===== Copy to clipboard =====
function copyToClipboard(text) {
    navigator.clipboard.writeText(text).then(() => {
        showToast('Copied to clipboard', 'success');
    });
}

// ===== Toast notification =====
function showToast(message, type = 'info') {
    const toast = document.createElement('div');
    toast.className = `alert alert-${type} alert-dismissible`;
    toast.style.cssText = 'position:fixed;top:1rem;right:1rem;z-index:9999;min-width:300px;animation:slideIn 0.3s ease';
    toast.innerHTML = `<span>${message}</span>`;
    document.body.appendChild(toast);
    setTimeout(() => {
        toast.style.opacity = '0';
        setTimeout(() => toast.remove(), 300);
    }, 3500);
}

// ===== Sidebar active state based on URL =====
(function highlightCurrentNav() {
    const links = document.querySelectorAll('.nav-link[href]');
    const path = window.location.pathname;
    links.forEach(link => {
        if (path === link.getAttribute('href') ||
            (link.getAttribute('href') !== '/' && path.startsWith(link.getAttribute('href')))) {
            link.classList.add('active');
        }
    });
})();
