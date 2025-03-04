/*!
 *
 * plugins/keyboardnav.js
 * Litepicker v2.0.12 (https://github.com/wakirin/Litepicker)
 * Package: litepicker (https://www.npmjs.com/package/litepicker)
 * License: MIT (https://github.com/wakirin/Litepicker/blob/master/LICENCE.md)
 * Copyright 2019-2021 Rinat G.
 *
 * Hash: fc3887e0bb19d54c36db
 *
 */ !(function (e) {
  var t = {};
  function n(r) {
    if (t[r]) return t[r].exports;
    var o = (t[r] = { i: r, l: !1, exports: {} });
    return e[r].call(o.exports, o, o.exports, n), (o.l = !0), o.exports;
  }
  (n.m = e),
    (n.c = t),
    (n.d = function (e, t, r) {
      n.o(e, t) || Object.defineProperty(e, t, { enumerable: !0, get: r });
    }),
    (n.r = function (e) {
      'undefined' != typeof Symbol && Symbol.toStringTag && Object.defineProperty(e, Symbol.toStringTag, { value: 'Module' }),
        Object.defineProperty(e, '__esModule', { value: !0 });
    }),
    (n.t = function (e, t) {
      if ((1 & t && (e = n(e)), 8 & t)) return e;
      if (4 & t && 'object' == typeof e && e && e.__esModule) return e;
      var r = Object.create(null);
      if ((n.r(r), Object.defineProperty(r, 'default', { enumerable: !0, value: e }), 2 & t && 'string' != typeof e))
        for (var o in e)
          n.d(
            r,
            o,
            function (t) {
              return e[t];
            }.bind(null, o)
          );
      return r;
    }),
    (n.n = function (e) {
      var t =
        e && e.__esModule
          ? function () {
              return e.default;
            }
          : function () {
              return e;
            };
      return n.d(t, 'a', t), t;
    }),
    (n.o = function (e, t) {
      return Object.prototype.hasOwnProperty.call(e, t);
    }),
    (n.p = ''),
    n((n.s = 0));
})([
  function (e, t, n) {
    'use strict';
    n.r(t);
    n(1);
    function r(e, t) {
      var n = Object.keys(e);
      if (Object.getOwnPropertySymbols) {
        var r = Object.getOwnPropertySymbols(e);
        t &&
          (r = r.filter(function (t) {
            return Object.getOwnPropertyDescriptor(e, t).enumerable;
          })),
          n.push.apply(n, r);
      }
      return n;
    }
    function o(e) {
      for (var t = 1; t < arguments.length; t++) {
        var n = null != arguments[t] ? arguments[t] : {};
        t % 2
          ? r(Object(n), !0).forEach(function (t) {
              i(e, t, n[t]);
            })
          : Object.getOwnPropertyDescriptors
          ? Object.defineProperties(e, Object.getOwnPropertyDescriptors(n))
          : r(Object(n)).forEach(function (t) {
              Object.defineProperty(e, t, Object.getOwnPropertyDescriptor(n, t));
            });
      }
      return e;
    }
    function i(e, t, n) {
      return t in e ? Object.defineProperty(e, t, { value: n, enumerable: !0, configurable: !0, writable: !0 }) : (e[t] = n), e;
    }
    Litepicker.add('keyboardnav', {
      init: function (e) {
        Object.defineProperties(e, { isMouseDown: { value: !1, writable: !0 } });
        function t(t, r) {
          if (t.classList.contains('day-item')) {
            r.preventDefault();
            var o = n(e.ui, t, function (e, t) {
              return e === (t = 'ArrowLeft' === r.code ? t - 1 : t + 1);
            });
            o
              ? o.focus()
              : (function (t) {
                  var n = e.ui.querySelector(
                    ''.concat({ ArrowLeft: '.button-previous-month', ArrowRight: '.button-next-month' }[t.code], '[tabindex="1"]')
                  );
                  n && n.dispatchEvent(new Event('click'));
                  setTimeout(function () {
                    var n = null;
                    switch (t.code) {
                      case 'ArrowLeft':
                        var r = e.ui.querySelectorAll('[tabindex="2"]');
                        n = r[r.length - 1];
                        break;
                      case 'ArrowRight':
                        n = e.ui.querySelector('[tabindex="2"]');
                    }
                    n.focus();
                  });
                })(r);
          }
        }
        function n(e, t, n) {
          var r = Array.from(e.querySelectorAll('.day-item[tabindex="2"]')),
            o = r.indexOf(t);
          return r.filter(function (e, t) {
            return n(t, o) && 2 === e.tabIndex;
          })[0];
        }
        function r(t) {
          e.isMouseDown = !0;
        }
        function i(t) {
          e.isMouseDown
            ? (e.isMouseDown = !1)
            : this.options.inlineMode ||
              this.isShowning() ||
              (this.show(t.target), this.ui.querySelector('[tabindex="'.concat(e.options.keyboardnav.firstTabIndex, '"]')).focus());
        }
        function c(e) {
          var t = this;
          this.options.inlineMode ||
            setTimeout(function () {
              var e = document.activeElement;
              t.ui.contains(e) || (t.nextFocusElement = e);
            });
        }
        (e.options.keyboardnav = o(o({}, { firstTabIndex: 1 }), e.options.keyboardnav)),
          e.ui.addEventListener(
            'keydown',
            function (r) {
              var o = this,
                i = r.target;
              switch (
                (setTimeout(function () {
                  o.onMouseEnter({ target: document.activeElement });
                }),
                r.code)
              ) {
                case 'ArrowUp':
                case 'ArrowDown':
                  !(function (t, r) {
                    if (t.classList.contains('day-item')) {
                      r.preventDefault();
                      var o = n(e.ui, t, function (e, t) {
                        return e === (t = 'ArrowUp' === r.code ? t - 7 : t + 7);
                      });
                      o && o.focus();
                    }
                  })(i, r);
                  break;
                case 'ArrowLeft':
                case 'ArrowRight':
                  t(i, r);
                  break;
                case 'Tab':
                  !(function (t, n) {
                    setTimeout(function () {
                      if (!document.activeElement.closest('.litepicker')) {
                        var n = e.ui.querySelector('[tabindex="1"]');
                        if (t === n) {
                          var r = e.ui.querySelectorAll('[tabindex="2"]');
                          n = r[r.length - 1];
                        }
                        n.focus();
                      }
                    });
                  })(i);
                  break;
                case 'Enter':
                case 'Space':
                  !(function (t, n) {
                    t.classList.contains('day-item') &&
                      (n.preventDefault(),
                      document.activeElement.dispatchEvent(new Event('click')),
                      setTimeout(function () {
                        var t = e.ui.querySelector('.is-start-date[tabindex="2"]');
                        t || (t = e.ui.querySelector('[tabindex="2"]')), t.focus();
                      }));
                  })(i, r);
                  break;
                case 'Escape':
                  e.hide();
              }
            }.bind(e),
            !0
          );
        var u = e.options;
        u.element instanceof HTMLElement &&
          (u.element.addEventListener('mousedown', r.bind(e), !0), u.element.addEventListener('focus', i.bind(e), !0)),
          u.elementEnd instanceof HTMLElement &&
            (u.elementEnd.addEventListener('mousedown', r.bind(e), !0), u.elementEnd.addEventListener('focus', i.bind(e), !0)),
          u.element instanceof HTMLElement && u.element.addEventListener('blur', c.bind(e), !0),
          u.elementEnd instanceof HTMLElement && u.elementEnd.addEventListener('blur', c.bind(e), !0),
          e.on('render', function (e) {
            Array.from(
              e.querySelectorAll(
                [
                  '.month-item:first-child:not(.no-previous-month) .button-previous-month',
                  '.month-item:last-child:not(.no-next-month) .button-next-month',
                  '.reset-button',
                  'select',
                ].join(',')
              )
            ).forEach(function (e) {
              return (e.tabIndex = 1);
            });
          }),
          e.on('render:day', function (e) {
            e.tabIndex = e.classList.contains('is-locked') ? -1 : 2;
          });
      },
    });
  },
  function (e, t, n) {},
]);
