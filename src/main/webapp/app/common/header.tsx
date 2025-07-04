import React, { useEffect, useRef } from 'react';
import { Link } from 'react-router';
import { useTranslation } from 'react-i18next';


export default function Header() {
  const { t } = useTranslation();
  const headerRef = useRef<HTMLElement|null>(null);

  const handleClick = (event: Event) => {
    // close any open dropdown
    const $clickedDropdown = (event.target as HTMLElement).closest('.js-dropdown');
    const $dropdowns = headerRef.current!.querySelectorAll('.js-dropdown');
    $dropdowns.forEach(($dropdown:Element) => {
      if ($clickedDropdown !== $dropdown && $dropdown.getAttribute('data-dropdown-keepopen') !== 'true') {
        $dropdown.ariaExpanded = 'false';
        $dropdown.nextElementSibling!.classList.add('hidden');
      }
    });
    // toggle selected if applicable
    if ($clickedDropdown) {
      $clickedDropdown.ariaExpanded = '' + ($clickedDropdown.ariaExpanded !== 'true');
      $clickedDropdown.nextElementSibling!.classList.toggle('hidden');
    }
  };

  useEffect(() => {
    document.body.addEventListener('click', handleClick);
    return () => document.body.removeEventListener('click', handleClick);
  }, []);

  return (
    <header ref={headerRef} className="bg-gray-50">
      <div className="container mx-auto px-4 md:px-6">
        <nav className="flex flex-wrap items-center justify-between py-2">
          <Link to="/" className="flex py-1.5 mr-4">
            <img src="/images/logo.png" alt={t('app.title')} width="30" height="30" className="inline-block" />
            <span className="text-xl pl-3">{t('app.title')}</span>
          </Link>
          <button type="button" className="js-dropdown md:hidden border rounded cursor-pointer" data-dropdown-keepopen="true"
              aria-label={t('navigation.toggle')} aria-controls="navbarToggle" aria-expanded="false">
            <div className="space-y-1.5 my-2.5 mx-4">
              <div className="w-6 h-0.5 bg-gray-500"></div>
              <div className="w-6 h-0.5 bg-gray-500"></div>
              <div className="w-6 h-0.5 bg-gray-500"></div>
            </div>
          </button>
          <div className="hidden md:block flex grow md:grow-0 justify-end basis-full md:basis-auto pt-3 md:pt-1 pb-1" id="navbarToggle">
            <ul className="flex">
              <li>
                <Link to="/" className="block text-gray-500 p-2">{t('navigation.home')}</Link>
              </li>
            </ul>
          </div>
        </nav>
      </div>
    </header>
  );
}
