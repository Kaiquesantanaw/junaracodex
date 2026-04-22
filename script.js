// ─── Navbar scroll ───
const navbar = document.getElementById('navbar');
window.addEventListener('scroll', () => {
  navbar.classList.toggle('scrolled', window.scrollY > 20);
});

// ─── Mobile menu ───
const hamburger = document.getElementById('hamburger');
const navLinks = document.querySelector('.nav-links');
hamburger.addEventListener('click', () => {
  navLinks.classList.toggle('open');
});
navLinks.querySelectorAll('a').forEach(link => {
  link.addEventListener('click', () => navLinks.classList.remove('open'));
});

// ─── Scroll reveal ───
const revealObserver = new IntersectionObserver((entries) => {
  entries.forEach((entry, i) => {
    if (entry.isIntersecting) {
      setTimeout(() => entry.target.classList.add('visible'), i * 80);
      revealObserver.unobserve(entry.target);
    }
  });
}, { threshold: 0.12 });

document.querySelectorAll(
  '.skill-category, .project-card, .plan-card, .contact-link, .sobre-card, .info-row'
).forEach(el => {
  el.classList.add('reveal');
  revealObserver.observe(el);
});

// ─── Active nav link on scroll ───
const sections = document.querySelectorAll('section[id]');
const navAnchors = document.querySelectorAll('.nav-links a');

const sectionObserver = new IntersectionObserver((entries) => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      navAnchors.forEach(a => a.style.color = '');
      const active = document.querySelector(`.nav-links a[href="#${entry.target.id}"]`);
      if (active) active.style.color = 'var(--text)';
    }
  });
}, { threshold: 0.4 });

sections.forEach(s => sectionObserver.observe(s));

// ─── Contact form ───
const form = document.getElementById('contactForm');
const submitBtn = document.getElementById('submitBtn');
const formSuccess = document.getElementById('formSuccess');

form.addEventListener('submit', (e) => {
  e.preventDefault();

  const nome = document.getElementById('nome').value.trim();
  const email = document.getElementById('email').value.trim();
  const mensagem = document.getElementById('mensagem').value.trim();

  if (!nome || !email || !mensagem) {
    shakeInvalid(form);
    return;
  }

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(email)) {
    document.getElementById('email').focus();
    shakeInvalid(document.getElementById('email'));
    return;
  }

  submitBtn.classList.add('loading');
  submitBtn.querySelector('span').textContent = 'Enviando...';

  // Simulate async send
  setTimeout(() => {
    submitBtn.classList.remove('loading');
    submitBtn.querySelector('span').textContent = 'Enviar mensagem';
    formSuccess.classList.add('show');
    form.reset();
    setTimeout(() => formSuccess.classList.remove('show'), 5000);
  }, 1500);
});

function shakeInvalid(el) {
  el.style.animation = 'shake 0.4s ease';
  setTimeout(() => el.style.animation = '', 400);
}

// ─── Inject shake keyframe ───
const style = document.createElement('style');
style.textContent = `
  @keyframes shake {
    0%,100%  { transform: translateX(0); }
    20%      { transform: translateX(-6px); }
    40%      { transform: translateX(6px); }
    60%      { transform: translateX(-4px); }
    80%      { transform: translateX(4px); }
  }
`;
document.head.appendChild(style);

// ─── Typing cursor in hero ───
const heroTitle = document.querySelector('.hero-title');
if (heroTitle) {
  // Subtle blink on the gradient text
  const gradientEl = heroTitle.querySelector('.gradient-text');
  if (gradientEl) {
    let show = true;
    setInterval(() => {
      gradientEl.style.opacity = (show = !show) ? '1' : '0.7';
    }, 2000);
  }
}

// ─── Smooth counter on stats ───
function animateCounter(el, target, duration = 1200) {
  const start = performance.now();
  const isPercent = el.dataset.suffix === '%';
  el.addEventListener('counterStart', () => {
    const update = (now) => {
      const elapsed = now - start;
      const progress = Math.min(elapsed / duration, 1);
      const eased = 1 - Math.pow(1 - progress, 3);
      el.textContent = Math.round(eased * target) + (isPercent ? '%' : '+');
      if (progress < 1) requestAnimationFrame(update);
    };
    requestAnimationFrame(update);
  });
}

const statEls = document.querySelectorAll('.stat strong');
const statsObserver = new IntersectionObserver((entries) => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      const el = entry.target;
      const raw = el.textContent;
      const num = parseInt(raw.replace(/\D/g, ''));
      el.dataset.suffix = raw.includes('%') ? '%' : '+';
      if (!isNaN(num)) {
        animateCounter(el, num);
        el.dispatchEvent(new Event('counterStart'));
      }
      statsObserver.unobserve(el);
    }
  });
}, { threshold: 0.8 });

statEls.forEach(el => statsObserver.observe(el));
