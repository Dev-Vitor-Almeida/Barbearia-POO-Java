const API_CONFIG = {
    baseUrl: "http://localhost:8080/api"
};

function showToast(message, type = "success") {
    const oldToast = document.querySelector(".toast");

    if (oldToast) {
        oldToast.remove();
    }

    const toast = document.createElement("div");
    toast.className = `toast toast-${type}`;
    toast.textContent = message;
    document.body.appendChild(toast);

    setTimeout(() => toast.classList.add("show"), 10);

    setTimeout(() => {
        toast.classList.remove("show");
        setTimeout(() => toast.remove(), 250);
    }, 3200);
}

function setButtonLoading(button, isLoading, loadingText = "Processando...") {
    if (!button) {
        return;
    }

    if (isLoading) {
        button.dataset.originalText = button.textContent;
        button.textContent = loadingText;
        button.disabled = true;
        return;
    }

    button.textContent = button.dataset.originalText || button.textContent;
    button.disabled = false;
}

function getFormData(form) {
    return Object.fromEntries(new FormData(form).entries());
}

function getLoggedUser() {
    const savedUser = localStorage.getItem("usuarioLogado");

    if (!savedUser) {
        return null;
    }

    try {
        return JSON.parse(savedUser);
    } catch (error) {
        localStorage.removeItem("usuarioLogado");
        return null;
    }
}

function isValidPhone(phone) {
    const digits = phone.replace(/\D/g, "");
    return digits.length >= 10 && digits.length <= 11;
}

function formatPhone(input) {
    const digits = input.value.replace(/\D/g, "").slice(0, 11);
    const hasNineDigits = digits.length > 10;
    const firstPart = digits.slice(0, 2);
    const secondPart = digits.slice(2, hasNineDigits ? 7 : 6);
    const thirdPart = digits.slice(hasNineDigits ? 7 : 6);

    if (digits.length <= 2) {
        input.value = firstPart;
        return;
    }

    if (!thirdPart) {
        input.value = `(${firstPart}) ${secondPart}`;
        return;
    }

    input.value = `(${firstPart}) ${secondPart}-${thirdPart}`;
}

function validateRequiredFields(form) {
    const fields = [...form.querySelectorAll("input[required], select[required]")];
    const emptyField = fields.find((field) => !field.value.trim());

    if (emptyField) {
        emptyField.focus();
        showToast("Preencha todos os campos obrigatorios.", "error");
        return false;
    }

    return true;
}

async function request(endpoint, options = {}) {
    const method = options.method || "GET";
    const payload = options.payload;
    const response = await fetch(`${API_CONFIG.baseUrl}${endpoint}`, {
        method,
        headers: {
            "Content-Type": "application/json"
        },
        body: payload ? JSON.stringify(payload) : undefined
    });

    if (!response.ok) {
        const error = await response.json().catch(() => null);
        throw new Error(error?.erro || "Nao foi possivel concluir a solicitacao.");
    }

    return response.json();
}

function setupHeaderScroll() {
    const topbar = document.querySelector(".topbar");

    if (!topbar) {
        return;
    }

    const updateHeader = () => {
        topbar.classList.toggle("is-scrolled", window.scrollY > 18);
    };

    updateHeader();
    window.addEventListener("scroll", updateHeader, { passive: true });
}

function setupRevealAnimations() {
    const elements = document.querySelectorAll(".hero-content, .card, .form-panel, .panel-heading");

    if (!elements.length) {
        return;
    }

    elements.forEach((element) => element.classList.add("reveal"));

    if (!("IntersectionObserver" in window)) {
        elements.forEach((element) => element.classList.add("is-visible"));
        return;
    }

    const observer = new IntersectionObserver((entries) => {
        entries.forEach((entry) => {
            if (entry.isIntersecting) {
                entry.target.classList.add("is-visible");
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.18 });

    elements.forEach((element) => observer.observe(element));
}

function setupButtonRipple() {
    const buttons = document.querySelectorAll(".btn");

    buttons.forEach((button) => {
        button.addEventListener("click", (event) => {
            const ripple = document.createElement("span");
            const rect = button.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height);

            ripple.className = "btn-ripple";
            ripple.style.width = `${size}px`;
            ripple.style.height = `${size}px`;
            ripple.style.left = `${event.clientX - rect.left - size / 2}px`;
            ripple.style.top = `${event.clientY - rect.top - size / 2}px`;

            button.appendChild(ripple);
            setTimeout(() => ripple.remove(), 550);
        });
    });
}

function setupServiceCards() {
    const serviceMap = {
        Corte: "corte",
        Barba: "barba",
        Sobrancelha: "sobrancelha"
    };

    document.querySelectorAll(".card").forEach((card) => {
        const title = card.querySelector("h3")?.textContent.trim();
        const service = serviceMap[title];

        if (!service) {
            return;
        }

        card.tabIndex = 0;
        card.setAttribute("role", "button");
        card.setAttribute("aria-label", `Agendar ${title}`);

        const goToSchedule = () => {
            window.location.href = `Agendamento.html?servico=${encodeURIComponent(service)}`;
        };

        card.addEventListener("click", goToSchedule);
        card.addEventListener("keydown", (event) => {
            if (event.key === "Enter" || event.key === " ") {
                event.preventDefault();
                goToSchedule();
            }
        });
    });
}

function setupAppointmentFromQuery() {
    const params = new URLSearchParams(window.location.search);
    const selectedService = params.get("servico");
    const serviceSelect = document.querySelector("#servico");

    if (!selectedService || !serviceSelect) {
        return;
    }

    const option = [...serviceSelect.options].find((item) => item.dataset.nome === selectedService);

    if (option) {
        serviceSelect.value = option.value;
        showToast(`${selectedService} selecionado para agendamento.`);
    }
}

function panelUrlFor(user) {
    if (!user) {
        return "Login.html";
    }

    return user.tipo === "barbeiro" ? "PainelBarbeiro.html" : "PainelCliente.html";
}

async function loadAppointmentOptions() {
    const serviceSelect = document.querySelector("#servico");
    const barberSelect = document.querySelector("#barbeiro");

    if (!serviceSelect && !barberSelect) {
        return;
    }

    try {
        const [servicos, barbeiros] = await Promise.all([
            request("/servicos"),
            request("/barbeiros")
        ]);

        if (serviceSelect) {
            serviceSelect.innerHTML = `<option value="">Selecione um servico</option>`;
            servicos.forEach((servico) => {
                const option = document.createElement("option");
                option.value = servico.id;
                option.dataset.nome = servico.nome;
                option.textContent = `${servico.nome} - R$ ${Number(servico.preco).toFixed(2)}`;
                serviceSelect.appendChild(option);
            });
        }

        if (barberSelect) {
            barberSelect.innerHTML = `<option value="">Escolha um profissional</option>`;
            barbeiros.forEach((barbeiro) => {
                const option = document.createElement("option");
                option.value = barbeiro.id;
                option.textContent = barbeiro.nome;
                barberSelect.appendChild(option);
            });
        }
    } catch (error) {
        showToast("Nao foi possivel carregar servicos e barbeiros.", "error");
    }
}

function setupLoginForm() {
    const form = document.querySelector("[data-form='login']");

    if (!form) {
        return;
    }

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        if (!validateRequiredFields(form)) {
            return;
        }

        const button = form.querySelector("button[type='submit']");
        const data = getFormData(form);
        const tipo = data.tipo || "cliente";
        delete data.tipo;

        try {
            setButtonLoading(button, true, "Entrando...");
            const usuario = await request(`/login/${tipo}`, {
                method: "POST",
                payload: data
            });
            localStorage.setItem("usuarioLogado", JSON.stringify({
                ...usuario,
                tipo
            }));
            showToast("Login realizado com sucesso.");
            setTimeout(() => {
                window.location.href = panelUrlFor({ tipo });
            }, 700);
        } catch (error) {
            showToast(error.message, "error");
        } finally {
            setButtonLoading(button, false);
        }
    });
}

function setupLoggedUserPanel() {
    const heading = document.querySelector(".panel-heading");

    if (!heading) {
        return;
    }

    const usuario = getLoggedUser();

    if (!usuario) {
        window.location.href = "Login.html";
        return;
    }

    const currentPage = window.location.pathname.split("/").pop();

    if (currentPage === "PainelCliente.html" && usuario.tipo !== "cliente") {
        window.location.href = "PainelBarbeiro.html";
        return;
    }

    if ((currentPage === "PainelBarbeiro.html" || currentPage === "Painel.html") && usuario.tipo !== "barbeiro") {
        window.location.href = "PainelCliente.html";
        return;
    }

    document.querySelectorAll("[data-admin-only]").forEach((element) => {
        element.hidden = usuario.tipo !== "barbeiro";
    });

    const info = document.createElement("p");
    info.className = "panel-user";
    info.textContent = `Logado como ${usuario.nome} (${usuario.tipo})`;
    heading.appendChild(info);
}

async function loadClientAppointments() {
    const list = document.querySelector("[data-list='client-appointments']");

    if (!list) {
        return;
    }

    const user = getLoggedUser();

    if (!user) {
        return;
    }

    try {
        const agendamentos = await request("/agendamentos");
        const meusAgendamentos = agendamentos.filter((item) => Number(item.clienteId) === Number(user.id));

        if (!meusAgendamentos.length) {
            list.innerHTML = `
                <article class="appointment-card">
                    <p class="eyebrow">Agenda vazia</p>
                    <h2>Nenhum horario marcado</h2>
                    <p class="muted-text">Quando voce fizer um agendamento, ele aparece aqui.</p>
                    <a class="btn btn-primary" href="Agendamento.html">Agendar horario</a>
                </article>
            `;
            return;
        }

        list.innerHTML = meusAgendamentos.map((agendamento) => `
            <article class="appointment-card">
                <p class="eyebrow">${agendamento.servicoNome}</p>
                <h2>${agendamento.data} as ${agendamento.horario}</h2>
                <p>Barbeiro: <strong>${agendamento.barbeiroNome}</strong></p>
                <p>Valor: R$ ${Number(agendamento.preco).toFixed(2)}</p>
                <span class="status-badge">Confirmado</span>
            </article>
        `).join("");
    } catch (error) {
        list.innerHTML = `
            <article class="appointment-card">
                <p>Nao foi possivel carregar seus agendamentos.</p>
            </article>
        `;
        showToast(error.message, "error");
    }
}

async function loadAppointments(filter = "") {
    const tableBody = document.querySelector("[data-list='agendamentos']");

    if (!tableBody) {
        return;
    }

    try {
        const user = getLoggedUser();
        const agendamentos = await request("/agendamentos");
        const normalizedFilter = user?.tipo === "cliente"
            ? user.nome.toLowerCase()
            : filter.trim().toLowerCase();
        const filteredAppointments = normalizedFilter
            ? agendamentos.filter((item) => item.clienteNome.toLowerCase().includes(normalizedFilter))
            : agendamentos;

        if (!filteredAppointments.length) {
            tableBody.innerHTML = `<tr><td colspan="6">Nenhum agendamento encontrado.</td></tr>`;
            return;
        }

        tableBody.innerHTML = filteredAppointments.map((agendamento) => `
            <tr>
                <td>${agendamento.clienteNome}</td>
                <td>${agendamento.barbeiroNome}</td>
                <td>${agendamento.servicoNome}</td>
                <td>${agendamento.data}</td>
                <td>${agendamento.horario}</td>
                <td>
                    <div class="table-actions">
                        ${user?.tipo === "barbeiro" ? `
                            <button class="btn btn-secondary btn-small" type="button" data-action="editar-horario" data-id="${agendamento.id}" data-horario="${agendamento.horario}">Alterar</button>
                            <button class="btn btn-danger btn-small" type="button" data-action="cancelar-agendamento" data-id="${agendamento.id}">Cancelar</button>
                        ` : `<span class="muted-text">Somente consulta</span>`}
                    </div>
                </td>
            </tr>
        `).join("");
    } catch (error) {
        tableBody.innerHTML = `<tr><td colspan="6">Nao foi possivel carregar os agendamentos.</td></tr>`;
        showToast(error.message, "error");
    }
}

function setupAppointmentsPanel() {
    const tableBody = document.querySelector("[data-list='agendamentos']");
    const filterForm = document.querySelector("[data-form='filtro-agendamentos']");

    if (!tableBody) {
        return;
    }

    const user = getLoggedUser();

    loadAppointments();

    if (filterForm && user?.tipo === "barbeiro") {
        filterForm.addEventListener("submit", (event) => {
            event.preventDefault();
            const data = getFormData(filterForm);
            loadAppointments(data.cliente || "");
        });
    }

    tableBody.addEventListener("click", async (event) => {
        const button = event.target.closest("button[data-action]");

        if (!button) {
            return;
        }

        if (user?.tipo !== "barbeiro") {
            showToast("Voce tem acesso somente para consultar seus agendamentos.", "error");
            return;
        }

        const id = button.dataset.id;

        if (button.dataset.action === "cancelar-agendamento") {
            const confirmDelete = window.confirm("Deseja cancelar este agendamento?");

            if (!confirmDelete) {
                return;
            }

            try {
                setButtonLoading(button, true, "Cancelando...");
                await request(`/agendamentos?id=${encodeURIComponent(id)}`, { method: "DELETE" });
                showToast("Agendamento cancelado com sucesso.");
                loadAppointments();
            } catch (error) {
                showToast(error.message, "error");
            }
        }

        if (button.dataset.action === "editar-horario") {
            const novoHorario = window.prompt("Digite o novo horario:", button.dataset.horario || "");

            if (!novoHorario) {
                return;
            }

            try {
                setButtonLoading(button, true, "Salvando...");
                await request("/agendamentos", {
                    method: "PATCH",
                    payload: {
                        id,
                        horario: novoHorario
                    }
                });
                showToast("Horario atualizado com sucesso.");
                loadAppointments();
            } catch (error) {
                showToast(error.message, "error");
            }
        }
    });
}

function setupBarberForm() {
    const form = document.querySelector("[data-form='barbeiro']");
    const phoneInput = document.querySelector("#telefone-barbeiro");

    if (phoneInput) {
        phoneInput.addEventListener("input", () => formatPhone(phoneInput));
    }

    if (!form) {
        return;
    }

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        if (!validateRequiredFields(form)) {
            return;
        }

        const data = getFormData(form);

        if (!isValidPhone(data.telefone)) {
            showToast("Digite um telefone valido.", "error");
            return;
        }

        const button = form.querySelector("button[type='submit']");

        try {
            setButtonLoading(button, true, "Cadastrando...");
            await request("/barbeiros", {
                method: "POST",
                payload: data
            });
            showToast("Barbeiro cadastrado com sucesso.");
            form.reset();
            loadAppointmentOptions();
        } catch (error) {
            showToast(error.message, "error");
        } finally {
            setButtonLoading(button, false);
        }
    });
}

function setupServiceForm() {
    const form = document.querySelector("[data-form='servico']");

    if (!form) {
        return;
    }

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        if (!validateRequiredFields(form)) {
            return;
        }

        const data = getFormData(form);
        const button = form.querySelector("button[type='submit']");

        try {
            setButtonLoading(button, true, "Cadastrando...");
            await request("/servicos", {
                method: "POST",
                payload: {
                    nome: data.nome,
                    preco: Number(data.preco),
                    duracaoMinutos: Number(data.duracaoMinutos)
                }
            });
            showToast("Servico cadastrado com sucesso.");
            form.reset();
            loadAppointmentOptions();
        } catch (error) {
            showToast(error.message, "error");
        } finally {
            setButtonLoading(button, false);
        }
    });
}

function setupRegisterForm() {
    const form = document.querySelector("[data-form='cadastro']");
    const phoneInput = document.querySelector("#telefone");

    if (phoneInput) {
        phoneInput.addEventListener("input", () => formatPhone(phoneInput));
    }

    if (!form) {
        return;
    }

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        if (!validateRequiredFields(form)) {
            return;
        }

        const data = getFormData(form);

        if (!isValidPhone(data.telefone)) {
            showToast("Digite um telefone valido.", "error");
            return;
        }

        const button = form.querySelector("button[type='submit']");

        try {
            setButtonLoading(button, true, "Cadastrando...");
            await request("/clientes", {
                method: "POST",
                payload: data
            });
            showToast("Cadastro criado com sucesso.");
            form.reset();
        } catch (error) {
            showToast(error.message, "error");
        } finally {
            setButtonLoading(button, false);
        }
    });
}

function setupAppointmentForm() {
    const form = document.querySelector("[data-form='agendamento']");
    const dateInput = document.querySelector("#data");
    const clientInput = document.querySelector("#cliente");
    const user = getLoggedUser();

    if (dateInput) {
        dateInput.min = new Date().toISOString().split("T")[0];
    }

    if (clientInput && user?.tipo === "cliente") {
        clientInput.value = user.nome;
        clientInput.readOnly = true;
    }

    if (!form) {
        return;
    }

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        if (!validateRequiredFields(form)) {
            return;
        }

        const data = getFormData(form);
        const selectedDateTime = new Date(`${data.data}T${data.horario}`);

        if (selectedDateTime < new Date()) {
            showToast("Escolha uma data e horario futuros.", "error");
            return;
        }

        const button = form.querySelector("button[type='submit']");
        const serviceSelect = document.querySelector("#servico");
        const barberSelect = document.querySelector("#barbeiro");
        const clienteLogado = getLoggedUser();

        if (!clienteLogado || clienteLogado.tipo !== "cliente") {
            showToast("Faca login como cliente para agendar.", "error");
            return;
        }

        try {
            setButtonLoading(button, true, "Agendando...");
            await request("/agendamentos", {
                method: "POST",
                payload: {
                    clienteId: clienteLogado.id,
                    barbeiroId: barberSelect.value,
                    servicoId: serviceSelect.value,
                    data: data.data,
                    horario: data.horario
                }
            });
            showToast("Horario agendado com sucesso.");
            form.reset();
            dateInput.min = new Date().toISOString().split("T")[0];
        } catch (error) {
            showToast(error.message, "error");
        } finally {
            setButtonLoading(button, false);
        }
    });
}

function setupActiveMenu() {
    const currentPage = window.location.pathname.split("/").pop() || "Index.html";
    const links = document.querySelectorAll(".menu a");

    links.forEach((link) => {
        const linkPage = link.getAttribute("href");
        link.classList.toggle("active", linkPage === currentPage);
    });
}

function setupAuthMenu() {
    const user = getLoggedUser();

    document.querySelectorAll("[data-auth-link]").forEach((link) => {
        link.hidden = !user;
        link.href = panelUrlFor(user);
    });

    document.querySelectorAll("[data-logout-link]").forEach((link) => {
        link.hidden = !user;
    });
}

function setupLogout() {
    document.querySelectorAll("[data-logout-link]").forEach((link) => {
        link.addEventListener("click", (event) => {
            event.preventDefault();
            localStorage.removeItem("usuarioLogado");
            showToast("Voce saiu da conta.");

            setTimeout(() => {
                window.location.href = "Login.html";
            }, 500);
        });
    });
}

document.addEventListener("DOMContentLoaded", () => {
    setupHeaderScroll();
    setupRevealAnimations();
    setupButtonRipple();
    setupServiceCards();
    setupAuthMenu();
    setupActiveMenu();
    setupLogout();
    setupLoggedUserPanel();
    loadAppointmentOptions().then(() => setupAppointmentFromQuery());
    setupLoginForm();
    setupRegisterForm();
    setupAppointmentForm();
    setupAppointmentsPanel();
    loadClientAppointments();
    setupBarberForm();
    setupServiceForm();
});

